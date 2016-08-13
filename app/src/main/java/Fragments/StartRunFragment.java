package Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.google.android.gms.fitness.result.SessionStopResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.nearby.messages.Distance;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import Utilities.DeviceHardwareChecker;
import allblacks.com.iBaleka.R;

public class StartRunFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private static final int RESOLVE_GPS_SETTINGS = 200;
    private static final int ACCESS_FINE_LOCATION_PERMISSION = 300;
    private MapView startRunMap;
    private TextView timeElapsedTextView;
    private TextView distanceTextView;
    private TextView speedTextView;
    private Button startRunButton;
    private Button endRunButton;
    private Button pauseRunButton;
    private boolean isGPSEnabled = false;
    private GoogleApiClient googleApiClient = null;
    private Location lastKnownLocation;
    private final static int REQUEST_GPS = 100;
    private boolean gpsGranted = false;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private GoogleMap mapObject;

    private OnDataPointListener locationListener;
    private OnDataPointListener speedListner;
    private OnDataPointListener cumulativeDistanceListener;
    private OnDataPointListener deltaDistanceListener;
    private OnDataPointListener caloriesListener;

    private Session fitnessSession;
    private boolean canOutputDataPoints = false;

    //Set up the needed data point stores

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_start_run, container, false);
        checkDeviceSettings();
        initializeComponents(currentView, savedInstanceState);
        handlePermissions();
        buildGoogleApiClient();
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        startRunMap = (MapView) currentView.findViewById(R.id.startRunMapLayout);
        startRunMap.onCreate(savedInstanceState);
        startRunMap.getMapAsync(this);
        timeElapsedTextView = (TextView) currentView.findViewById(R.id.elapsedTimeTextView);
        distanceTextView = (TextView) currentView.findViewById(R.id.distanceCoveredTextView);
        speedTextView = (TextView) currentView.findViewById(R.id.runnerSpeedTextView);
        startRunButton = (Button) currentView.findViewById(R.id.startRunButton);
        pauseRunButton = (Button) currentView.findViewById(R.id.pauseRunButton);
        endRunButton = (Button) currentView.findViewById(R.id.endRunButton);
        startRunButton.setOnClickListener(this);
        endRunButton.setOnClickListener(this);
        pauseRunButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        buildGoogleApiClient();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapObject = googleMap;

    }

    private void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Toast.makeText(getActivity(), "Connected to Google", Toast.LENGTH_SHORT).show();
                        getFitnessDataSources();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        googleApiClient.connect();
                    }
                }).build();
        googleApiClient.connect();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    private void getFitnessDataSources()
    {
        //Get the location data source
        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
        .setDataTypes(DataType.TYPE_LOCATION_SAMPLE)
        .setDataSourceTypes(DataSource.TYPE_DERIVED)
        .build())
        .setResultCallback(new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                for (DataSource foundSource : dataSourcesResult.getDataSources()) {
                    Log.i("Data Source Found: ", foundSource.getDataType().getName());
                    registerFitnessDataListener(foundSource.getDataType(), foundSource);
                }
            }
        });

        //Get the calories expended data source
        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
        .setDataTypes(DataType.TYPE_CALORIES_EXPENDED)
        .setDataSourceTypes(DataSource.TYPE_DERIVED)
        .build()).setResultCallback(new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                for (DataSource foundSource : dataSourcesResult.getDataSources()) {
                    Log.i("Data Source Found: ", foundSource.getDataType().getName());
                    registerFitnessDataListener(foundSource.getDataType(), foundSource);
                }
            }
        });

        //Get the speed data source
        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
        .setDataTypes(DataType.TYPE_SPEED)
        .setDataSourceTypes(DataSource.TYPE_RAW)
        .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                        for (DataSource foundSource : dataSourcesResult.getDataSources()) {
                            Log.i("Data Source Found: ", foundSource.getDataType().getName());
                            registerFitnessDataListener(foundSource.getDataType(), foundSource);
                        }
                    }
                });

        //Get the cumulative distance data source
        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
        .setDataTypes(DataType.TYPE_DISTANCE_CUMULATIVE)
        .setDataSourceTypes(DataSource.TYPE_RAW)
        .build()).setResultCallback(new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                for (DataSource foundSource : dataSourcesResult.getDataSources()) {
                    Log.i("Data Source Found: ", foundSource.getDataType().getName());
                    registerFitnessDataListener(foundSource.getDataType(), foundSource);
                }
            }
        });

        //Get the distance at that point
        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
        .setDataTypes(DataType.TYPE_DISTANCE_DELTA)
        .setDataSourceTypes(DataSource.TYPE_RAW)
        .build()).setResultCallback(new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                for (DataSource foundSource : dataSourcesResult.getDataSources()) {
                    Log.i("Data Source Found: ", foundSource.getDataType().getName());
                    registerFitnessDataListener(foundSource.getDataType(), foundSource);
                }
            }
        });
    }

    private void checkDeviceSettings()
    {
        DeviceHardwareChecker checker = new DeviceHardwareChecker(getActivity());
        checker.checkGPSReceiver();
        checker.checkNetworkConnection();
        if (!checker.getGPSStatus() || !checker.isConnectedToInternet()) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager mgr = getFragmentManager();
                    mgr.popBackStack();
                }
            };
            displayMessage("GPS and Internet Required", "To start running, we need your GPS and Internet connection. Please turn these on", listener);
        }
    }

    private void registerFitnessDataListener(DataType type, final DataSource source) {

        if (type.equals(DataType.TYPE_LOCATION_SAMPLE)){
            locationListener = new OnDataPointListener() {
                @Override
                public void onDataPoint(DataPoint dataPoint) {
                    for (final Field field : dataPoint.getDataSource().getDataType().getFields()) {
                        final Value fieldValue = dataPoint.getValue(field);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Location Field Name", field.getName());
                                Log.i("Location Field Value", Float.toString(fieldValue.asFloat()));
                                displayToast("Loc. Value: " +Float.toString(fieldValue.asFloat()));

                            }
                        });
                    }
                }
            };

            Fitness.SensorsApi.add(googleApiClient, new SensorRequest.Builder()
            .setDataType(type)
            .setDataSource(source)
            .setSamplingRate(5, TimeUnit.SECONDS)
            .build(), locationListener)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.i("Registered", "Location Listener has been registered");
                            } else {
                                Log.i("Error", "Location Listener not registered" + status.getStatusMessage());
                            }
                        }
                    });
            createFitnessSubscription(type);
        } else if (type.equals(DataType.TYPE_CALORIES_EXPENDED)) {
            caloriesListener = new OnDataPointListener() {
                @Override
                public void onDataPoint(DataPoint dataPoint) {
                    for (final Field field : dataPoint.getDataSource().getDataType().getFields()) {
                        final Value fieldValue = dataPoint.getValue(field);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Calories Field Name", field.getName());
                                Log.i("Calories Field Value", Float.toString(fieldValue.asFloat()));
                                displayToast("Calories Value: " +Float.toString(fieldValue.asFloat()));
                            }
                        });
                    }
                }
            };

            Fitness.SensorsApi.add(googleApiClient, new SensorRequest.Builder()
                    .setDataType(type)
                    .setDataSource(source)
                    .setSamplingRate(1, TimeUnit.SECONDS)
                    .build(), caloriesListener)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.i("Registered", "Location Listener has been registered");
                            } else {
                                Log.i("Error", "Location Listener not registered" + status.getStatusMessage());
                            }
                        }
                    });
            createFitnessSubscription(type);
        } else if (type.equals(DataType.TYPE_SPEED)) {
            speedListner = new OnDataPointListener() {
                @Override
                public void onDataPoint(DataPoint dataPoint) {
                    for (final Field field : dataPoint.getDataSource().getDataType().getFields()) {
                        final Value fieldValue = dataPoint.getValue(field);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Speed Field Name", field.getName());
                                Log.i("Speed Field Value", Float.toString(fieldValue.asFloat()));
                                displayToast("Speed Value: " +Float.toString(fieldValue.asFloat()));
                            }
                        });
                    }
                }
            };

            Fitness.SensorsApi.add(googleApiClient, new SensorRequest.Builder()
                    .setDataType(type)
                    .setDataSource(source)
                    .setSamplingRate(1, TimeUnit.SECONDS)
                    .build(), speedListner)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.i("Registered", "Location Listener has been registered");
                            } else {
                                Log.i("Error", "Location Listener not registered" + status.getStatusMessage());
                            }
                        }
                    });
            createFitnessSubscription(type);
        } else if (type.equals(DataType.TYPE_DISTANCE_CUMULATIVE)) {
            cumulativeDistanceListener = new OnDataPointListener() {
                @Override
                public void onDataPoint(DataPoint dataPoint) {
                    for (final Field field : dataPoint.getDataSource().getDataType().getFields()) {
                        final Value fieldValue = dataPoint.getValue(field);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Cum.Distance Field Name", field.getName());
                                Log.i("Cu Distance Field Value", Float.toString(fieldValue.asFloat()));
                                displayToast("Cum. Distance: " +Float.toString(fieldValue.asFloat()));
                            }
                        });
                    }
                }
            };

            Fitness.SensorsApi.add(googleApiClient, new SensorRequest.Builder()
                    .setDataType(type)
                    .setDataSource(source)
                    .setSamplingRate(1, TimeUnit.SECONDS)
                    .build(), cumulativeDistanceListener)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.i("Registered", "Location Listener has been registered");
                            } else {
                                Log.i("Error", "Location Listener not registered" + status.getStatusMessage());
                            }
                        }
                    });
            createFitnessSubscription(type);
        } else if (type.equals(DataType.TYPE_DISTANCE_DELTA)) {
            deltaDistanceListener = new OnDataPointListener() {
                @Override
                public void onDataPoint(DataPoint dataPoint) {
                    for (final Field field : dataPoint.getDataSource().getDataType().getFields()) {
                        final Value fieldValue = dataPoint.getValue(field);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Delta Dist Field Name", field.getName());
                                Log.i("Delt Dist Field Value", Float.toString(fieldValue.asFloat()));
                                displayToast("Dist. Val: " +Float.toString(fieldValue.asFloat()));
                            }
                        });
                    }
                }
            };

            Fitness.SensorsApi.add(googleApiClient, new SensorRequest.Builder()
                    .setDataType(type)
                    .setDataSource(source)
                    .setSamplingRate(1, TimeUnit.SECONDS)
                    .build(), deltaDistanceListener)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.i("Registered", "Location Listener has been registered");
                            } else {
                                Log.i("Error", "Location Listener not registered" + status.getStatusMessage());
                            }
                        }
                    });
            createFitnessSubscription(type);
        }
    }

    private void createFitnessSession()
    {

        if (fitnessSession == null) {
            long timeInMills = System.currentTimeMillis();
            long sessionStartTime = Calendar.getInstance().getTimeInMillis();
            fitnessSession = new Session.Builder()
                    .setName("iBaleka Fitness Session")
                    .setIdentifier(getResources().getString(R.string.app_name) + " " + timeInMills)
                    .setDescription("iBaleka Personal Run")
                    .setStartTime(sessionStartTime, TimeUnit.MILLISECONDS)
                    .setActivity(FitnessActivities.RUNNING)
                    .build();

            PendingResult<Status> pendingResult = Fitness.SessionsApi.startSession(googleApiClient, fitnessSession);

            pendingResult.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        Log.i("Session Created", "Fitness Session has been created");
                    } else {
                        Log.i("Session Failed", "Session Failed " + status.getStatusMessage());
                    }
                }
            });

           /* //Insert the session into the fitness store
            SessionInsertRequest insertRequest = new SessionInsertRequest.Builder()
                    .setSession(fitnessSession)
                    .build();

            Fitness.SessionsApi.insertSession(googleApiClient, insertRequest).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.getStatus().isSuccess()) {
                        Log.i("Success", "The Session has been inserted into the fitness store");
                    } else {
                        Log.i("Error", "Error Inserting the fitness to fitness store " +status.getStatusMessage());
                    }
                }
            });


            //Create the fitness read request to read data from the session
            SessionReadRequest readRequest = new SessionReadRequest.Builder()
                    .setSessionName("iBaleka Fitness Session")
                    .setSessionId(getResources().getString(R.string.app_name) + " " +timeInMills)
                    .read(DataType.TYPE_SPEED)
                    .read(DataType.TYPE_LOCATION_SAMPLE)
                    .read(DataType.TYPE_CALORIES_EXPENDED)
                    .read(DataType.TYPE_DISTANCE_DELTA)
                    .read(DataType.TYPE_DISTANCE_CUMULATIVE)
                    .build();
            SessionReadResult readResult = Fitness.SessionsApi.readSession(googleApiClient, readRequest).await(1, TimeUnit.SECONDS);
*/
        }
    }

    private void unregisterListeners()
    {
        Fitness.SensorsApi.remove(googleApiClient, locationListener);
        Fitness.SensorsApi.remove(googleApiClient, deltaDistanceListener);
        Fitness.SensorsApi.remove(googleApiClient, cumulativeDistanceListener);
        Fitness.SensorsApi.remove(googleApiClient, speedListner);
        Fitness.SensorsApi.remove(googleApiClient, caloriesListener);
    }

    private void createFitnessSubscription(final DataType dataType) {
        Fitness.RecordingApi.subscribe(googleApiClient, dataType).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()) {
                    if (status.getStatusCode() == FitnessStatusCodes.SUCCESS) {
                        Log.i("Subscribed", "Subscribed to Fitness Data "+dataType.getName());
                    } else if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                        Log.i("Already Subscribed", "You already subscribed to Fitness "+dataType.getName());
                    } else {
                        Log.i("Error Subscribing", "There was an error subscribing "+status.getStatusMessage());
                    }
                }
            }
        });
    }

    private void destroyFitnessSession()
    {
        PendingResult<SessionStopResult> pendingResult = Fitness.SessionsApi.stopSession(googleApiClient, fitnessSession.getIdentifier());
        pendingResult.setResultCallback(new ResultCallback<SessionStopResult>() {
            @Override
            public void onResult(@NonNull SessionStopResult sessionStopResult) {
                if (sessionStopResult.getStatus().isSuccess()) {
                    Toast.makeText(getActivity(), "Session Stopped", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Error Session", "Error ending session "+sessionStopResult.getStatus().getStatusMessage());
                }
            }
        });
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", listener);
        messageBox.show();
    }

    private void handlePermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    buildGoogleApiClient();
                } else {
                    requestPermissions(new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION);
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                displayMessage("Start Running Cannot Continue", "The system cannot operate this function until access is granted to use your GPS receiver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentManager mgr = getFragmentManager();
                        mgr.popBackStack();
                    }
                });
            }
        }
    }

    private void displayToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.startRunButton:
                createFitnessSession();
                break;
            case R.id.endRunButton:
                destroyFitnessSession();
                unregisterListeners();
                break;
        }
    }
}