package Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import allblacks.com.iBaleka.R;

public class StartRunFragment extends Fragment {

    private static final int REQUIRED_SIGN_IN = 100;
    private MapView startRunMapView;
    private Button startRunButton;
    private Button pauseRunButton;
    private Button endRunButton;
    private TextView elapsedTimeTextView;
    private TextView distanceCoveredTextView;
    private TextView caloriesBurntTextView;
    private TextView averageSpeedTextView;

    private GoogleApiClient googleApiClient = null;
    private LocationRequest locationRequest;
    private Location currentLocation;

    private boolean canContinue = false;

    //Our data point listeners
    private OnDataPointListener caloriesListener;
    private OnDataPointListener speedListener;
    private OnDataPointListener cummulativeDistanceListener;
    private List<Float> cummulativeDistance;
    private List<Float> speedList;
    private List<Float> caloriesList = new ArrayList<>();

    private GoogleMap startRunMap;
    //Fitness Session
    private Session fitnessSession;
    private final static int REQUEST_FIT_AUTHORIZATION = 1;
    String registered = "The Following Have Been Registered: ";



    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_start_run, container, false);
        initializeComponents(currentView, savedInstanceState);
        cummulativeDistance = new ArrayList<Float>();
        speedList = new ArrayList<>();
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        startRunMapView = (MapView) currentView.findViewById(R.id.startRunMapLayout);
        startRunMapView.onCreate(savedInstanceState);
        startRunButton = (Button) currentView.findViewById(R.id.startRunButton);
        endRunButton = (Button) currentView.findViewById(R.id.endRunButton);
        pauseRunButton = (Button) currentView.findViewById(R.id.pauseRunButton);
        elapsedTimeTextView = (TextView) currentView.findViewById(R.id.elapsedTimeText);
        distanceCoveredTextView = (TextView) currentView.findViewById(R.id.distanceCoveredTextView);
        caloriesBurntTextView = (TextView) currentView.findViewById(R.id.totalCaloriesBurntTextView);
        averageSpeedTextView = (TextView) currentView.findViewById(R.id.runnerSpeedTextView);
        if (!checkPermissions()) {
            requestPermissions();
        }
    }
    private boolean checkPermissions()
    {
        int permissionState = ActivityCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions(this.getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_FIT_AUTHORIZATION);
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FIT_AUTHORIZATION);
        }
    }
    private void buildGoogleApi() {

        if (googleApiClient == null && checkPermissions()) {
            googleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .addApi(Fitness.SENSORS_API)
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.RECORDING_API)
                    .addApi(LocationServices.API)
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            try {
                                connectionResult.startResolutionForResult(getActivity(), REQUIRED_SIGN_IN);
                            } catch (Exception error) {

                            }
                        }
                    })
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            displayToast("Connected to the Google Service Successfully!");
                            getAvailableDataSources();
                            buildLocationRequest();
                            buildLocationSettings();
                            receiveLocationUpdates();


                            startRunMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    startRunMap = googleMap;
                                    receiveLocationUpdates();
                                    startRunMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                    startRunMap.setMyLocationEnabled(true);

                                }
                            });
                        }
                        @Override
                        public void onConnectionSuspended(int i) {
                            if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                displayMessage("Network Error", "iBaleka is unable to connect due to a network error");
                            } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                displayMessage("Disconnected", "The App has been disconnected from network service");
                            }
                        }
                    })
                    .build();
            googleApiClient.connect();
        }
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettings() {
        LocationSettingsRequest.Builder locationSettings = new LocationSettingsRequest.Builder();
        locationSettings.addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> locationResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettings.build());
        locationResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status locationResult = locationSettingsResult.getStatus();
                switch (locationResult.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        canContinue = true;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:


                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });
    }

    private void receiveLocationUpdates() {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    currentLocation = location;
                    if (startRunMap != null) {
                        startRunMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 19));
                    }
                }
            };
            requestPermissions();
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FIT_AUTHORIZATION:
                for (int a = 0; a < permissions.length; a++) {
                    if (grantResults[a] == PackageManager.PERMISSION_GRANTED) {
                        canContinue = true;
                    } else {
                        canContinue = false;
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onPause() {
        startRunMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        buildGoogleApi();
        startRunMapView.onResume();
    }

    @Override
    public void onDestroy() {
        startRunMapView.onDestroy();
        super.onDestroy();
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this.getActivity());
        alertBox.setTitle(title);
        alertBox.setMessage(message);
        alertBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertBox.show();
    }


    private void registerFitnessDataListener(final DataType dataType, final DataSource source) {

                if (dataType.equals(DataType.TYPE_SPEED)) {
                    speedListener = new OnDataPointListener() {
                        @Override
                        public void onDataPoint(DataPoint dataPoint) {
                            for (Field field : dataPoint.getDataType().getFields()) {
                                Value val = dataPoint.getValue(field);
                                displayToast("Data Point Detected (Speed): "+field.getName() + "["+val+"]");
                                speedList.add(val.asFloat());
                                updateLabels();
                            }
                        }
                    };
                    Fitness.SensorsApi.add(googleApiClient, new SensorRequest.Builder()
                                    .setDataSource(source)
                                    .setDataType(dataType)
                                    .setFastestRate(2, TimeUnit.SECONDS)
                                    .setAccuracyMode(SensorRequest.ACCURACY_MODE_HIGH)
                                    .setSamplingRate(10, TimeUnit.SECONDS)
                                    .build(),
                            speedListener)
                            .setResultCallback(new ResultCallback<Status>() {

                                @Override
                                public void onResult(@NonNull Status status) {
                                    if(status.isSuccess()) {
                                        displayToast("Speed Listener Registered");
                                    } else {
                                        displayToast("Speed Not Registered");
                                    }
                                }
                            });
                }

                if (dataType.equals(DataType.TYPE_CALORIES_EXPENDED)) {
                    caloriesListener = new OnDataPointListener() {
                        @Override
                        public void onDataPoint(DataPoint dataPoint) {
                            for (Field field : dataPoint.getDataType().getFields()) {
                                 Value val = dataPoint.getValue(field);
                                        caloriesList.add(val.asFloat());
                                        displayToast("Data Point Detected (Calories): "+field.getName() + "["+val+"]");
                                        updateLabels();
                                    }



                        }
                    };
                    Fitness.SensorsApi.add(googleApiClient, new SensorRequest.Builder()
                                    .setDataSource(source)
                                    .setDataType(dataType)
                                    .setAccuracyMode(SensorRequest.ACCURACY_MODE_HIGH)
                                    .setSamplingRate(3, TimeUnit.SECONDS)
                                    .build(),
                            caloriesListener)
                            .setResultCallback(new ResultCallback<Status>() {

                                @Override
                                public void onResult(@NonNull Status status) {
                                    if(status.isSuccess()) {
                                        displayToast("Caloies Listener Registered");
                                    } else {
                                        displayToast("Calories Not Registered");
                                    }
                                }
                            });
                }

                if (dataType.equals(DataType.TYPE_DISTANCE_CUMULATIVE)) {
                    cummulativeDistanceListener = new OnDataPointListener() {
                        @Override
                        public void onDataPoint(DataPoint dataPoint) {
                            for (Field field : dataPoint.getDataType().getFields()) {
                                Value val = dataPoint.getValue(field);
                                        cummulativeDistance.add(val.asFloat());
                                        displayToast("Data Point Detected (Distance): "+field.getName() + "["+val+"]");
                                        updateLabels();
                                    }

                        }
                    };
                    Fitness.SensorsApi.add(googleApiClient, new SensorRequest.Builder()
                                    .setDataSource(source)
                                    .setDataType(dataType)
                                    .setSamplingRate(10, TimeUnit.SECONDS)
                                    .setAccuracyMode(SensorRequest.ACCURACY_MODE_HIGH)
                                    .setFastestRate(5, TimeUnit.SECONDS)
                                    .build(),
                            cummulativeDistanceListener)
                            .setResultCallback(new ResultCallback<Status>() {

                                @Override
                                public void onResult(@NonNull Status status) {
                                    if(status.isSuccess()) {
                                        displayToast("Distance Listener is Registered");
                                    } else {
                                        displayToast("Distance Not Registered");
                                    }
                                }
                            });
                }
    }

    private void displayToast(String s) {
        Toast.makeText(this.getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void getAvailableDataSources()
    {
        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_SPEED)
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build()).setResultCallback(new ResultCallback<DataSourcesResult>() {

            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
             for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                 displayToast("Data Source Found: "+dataSource.getName());
                 if (dataSource.getDataType().equals(DataType.TYPE_SPEED) && speedListener == null) {
                     registerFitnessDataListener(DataType.TYPE_SPEED, dataSource);
                 }
             }
            }
        });

        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_CALORIES_EXPENDED)
                .setDataSourceTypes(DataSource.TYPE_DERIVED)
                .build()
        ).setResultCallback(new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                    displayToast("Data Source Found: "+dataSource.getName());

                    if (dataSource.getDataType().equals(DataType.TYPE_CALORIES_EXPENDED) && caloriesListener == null)
                    {
                        registerFitnessDataListener(DataType.TYPE_CALORIES_EXPENDED, dataSource);
                    }
                }
            }
        });

        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_DISTANCE_CUMULATIVE)
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build()
        ).setResultCallback(new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                    displayToast("Data Source Found: "+dataSource.getName());

                    if (dataSource.getDataType().equals(DataType.TYPE_DISTANCE_CUMULATIVE) && cummulativeDistanceListener == null)
                    {
                        registerFitnessDataListener(DataType.TYPE_DISTANCE_CUMULATIVE, dataSource);
                    }
                }
            }
        });

    }

    private void updateLabels()
    {
        distanceCoveredTextView.setText(cummulativeDistance.size());
        caloriesBurntTextView.setText(caloriesList.size());
        averageSpeedTextView.setText(speedList.size());
    }


    private void createFitnessSession()
    {
        fitnessSession = new Session.Builder()
                .setName("iBaleka Running Session")
                .setIdentifier(getString(R.string.app_name) + " " + System.currentTimeMillis())
                .setDescription("iBaleka Running Session")
                .setStartTime(Calendar.getInstance().getTimeInMillis(), TimeUnit.MILLISECONDS)
                .setActivity(FitnessActivities.RUNNING_JOGGING)
                .build();
    }

    private void stopFitnessSession()
    {
        Fitness.SessionsApi.stopSession(googleApiClient, "iBaleka Running Session");
    }

}