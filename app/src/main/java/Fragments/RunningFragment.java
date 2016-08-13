package Fragments;
import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.hardware.SensorEventListener;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import Utilities.DataContainer;
import Utilities.DeviceHardwareChecker;
import Utilities.Stopwatch;
import allblacks.com.iBaleka.R;

public class RunningFragment extends Fragment implements LocationListener, View.OnClickListener, SensorEventListener {

    private static final int ACCESS_FINE_LOCATION_PERMISSION = 300;
    private static final int RESOLUTION_RESULT = 200;
    private static final int ACCESS_GPS_SIGNALS = 400;
    private GoogleApiClient googleApiClient;
    private TextView timeTextView;
    private TextView distanceTextView;
    private TextView caloriesTextView;
    private TextView speedTextView;
    private MapView runningMapView;
    private Button startButton, stopButton;
    private boolean runStarted = false;
    private boolean runPaused = false;
    private boolean runCompleted = false;
    public static final int RESOLVE_GPS_RESULT = 500;
    private DateFormat dateFormat;
    private Date startDateTime;
    private Date endDateTime;
    private Location lastKnownLocation;
    private GoogleMap mapObject;
    private TextView toolbarTextView;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private List<Double> recordedSpeeds;
    private List<Float> distanceCovered;
    private Stopwatch stopwatchObject;
    long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    android.os.Handler handler = new android.os.Handler();
    private Double totalDistance = 0.0;
    private Double totalCalories = 0.0;
    private List<Location> locationsList; //A list of all locations on the route (regardless of pausing or running)
    private List<Location> runnerLocations; //A list of all points the runner has ran
    private String[] timesString = new String[3];

    //Solution: http://stackoverflow.com/documentation/android/3344/sensormanager#t=201608121519152716905
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private float movementThreshold = 0.5f;
    private boolean isMoving = false;
    private float [] previousValues = {1.0f, 1.0f, 1.0f};
    private float [] currentValues = new float[3];

    public RunningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_running, container, false);
        buildGoogleApiClient();
        createLocationRequest();
        checkDeviceSettings();
        initializeComponents(currentView, savedInstanceState);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        locationsList = new ArrayList<>();
        runnerLocations = new ArrayList<>();
        intializeDeviceSensors();
        return currentView;
    }

    private void intializeDeviceSensors()
    {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            recordedSpeeds = new ArrayList<>();
            stopwatchObject = new Stopwatch();
            distanceCovered = new ArrayList<>();
        }
        distanceTextView = (TextView) currentView.findViewById(R.id.RunningDistanceCoveredTextView);
        caloriesTextView = (TextView) currentView.findViewById(R.id.RunningTotalCaloriesBurntTextView);
        timeTextView = (TextView) currentView.findViewById(R.id.RunningElapsedTimeTextView);
        runningMapView = (MapView) currentView.findViewById(R.id.RunningMapLayout);
        speedTextView = (TextView) currentView.findViewById(R.id.RunningRunnerSpeedTextView);
        speedTextView.setText("0.0");
        runningMapView.onCreate(savedInstanceState);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("Start Running");
        runningMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapObject = googleMap;
                mapObject.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mapObject.animateCamera(CameraUpdateFactory.zoomBy(18));
            }
        });
        startButton = (Button) currentView.findViewById(R.id.RunningStartRunButton);
        startButton.setOnClickListener(this);
        stopButton = (Button) currentView.findViewById(R.id.RunningEndRunButton);
        stopButton.setOnClickListener(this);
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                displayToast("Connected to Google");
                receiveLocationUpdates();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                try {
                    connectionResult.startResolutionForResult(getActivity(), RESOLUTION_RESULT);
                    googleApiClient.connect();
                } catch (Exception error) {

                }
            }
        }).addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    private void displayToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private void checkDeviceSettings() {
        DeviceHardwareChecker checker = new DeviceHardwareChecker(getActivity());
        checker.checkNetworkConnection();
        checker.checkGPSReceiver();
        if (!checker.isConnectedToInternet()) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager mgr = getFragmentManager();
                    mgr.popBackStack();
                }
            };
            displayMessage("Network Required", "The application requires your GPS and Internet to be enabled", listener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                createLocationRequest();
                receiveLocationUpdates();

            } else if (requestCode == ACCESS_GPS_SIGNALS)
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest();
                    receiveLocationUpdates();
                }
            }
            else{
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

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates locationStates = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        receiveLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), RESOLVE_GPS_RESULT);
                        } catch (Exception error) {
                        }
                        break;
                    case LocationSettingsStatusCodes.CANCELED:
                        displayMessage("GPS Required", "You have denied access to GPS. Please turn on your GPS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager mgr = getFragmentManager();
                                mgr.popBackStack();
                            }
                        });
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        displayMessage("GPS Required", "You have denied access to GPS. Please turn on your GPS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager mgr = getFragmentManager();
                                mgr.popBackStack();
                            }
                        });
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESOLVE_GPS_RESULT) {
            if (resultCode == LocationSettingsStatusCodes.SUCCESS) {
                receiveLocationUpdates();
            }
        } else {

        }
    }

    private void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
    private void receiveLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
           ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, RESOLVE_GPS_RESULT);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", listener);
        messageBox.show();
    }

    private void calculateDistanceRan()
    {
        double distance = 0;
        if (runnerLocations.size() > 1) {
            for (int a = 1; a < runnerLocations.size() - 2; a++) {
                Location firstLocation = runnerLocations.get(a);
                Location nextLocation = runnerLocations.get(a + 1);
                distance = distance + firstLocation.distanceTo(nextLocation);
            }
            DecimalFormat format = new DecimalFormat("#.##");
            totalDistance = Double.valueOf(format.format(distance));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //For the first location update, we will assume this is the starting point
        if (currentLocation == null) {
            currentLocation = location;
        }
        float distance = 0L;
        double height = 0;
        double speed = 0;
        height = location.getAltitude();

        if (runStarted && !runPaused && isMoving) {
            locationsList.add(location); //This adds the current location to the list
            runnerLocations.add(location); //This adds the current route the runner is running (without pauses)
        } else if (isMoving){
            locationsList.add(location);
        }

        //Calculate the distance from the last recorded gps point
        if (currentLocation != null && runStarted && !runPaused){
            distance = currentLocation.distanceTo(location);
        }
        currentLocation = location;

        //move the map view to the user's current location, regardless of their running state (paused or run)
        mapObject.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));

        if (runStarted && !runPaused) {
            if (location.getSpeed() > 0) {
                speed = location.getSpeed() * 3.6;// convert m/sec to km/h
            }
            DecimalFormat format = new DecimalFormat("###.##");
            speed = Double.valueOf(format.format(speed));
            recordedSpeeds.add(speed);
            totalDistance = totalDistance + distance;
            updateUI(speed, location, distance);
            if (height > 0) {
                height = Double.valueOf(format.format(height));
            }
            calculateCurrentCaloriesBurnt(height, speed);
        }
    }

    private void updateUI(double speed, Location location, float thisDistance) {
        //totalDistance += thisDistance; Why the fuck did I do this twice!! What was i thinking!!
        DecimalFormat format = new DecimalFormat("###.##");
        totalDistance = Double.valueOf(format.format(totalDistance));
        mapObject.clear();
        mapObject.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition position = new CameraPosition.Builder().bearing(location.getBearing()).zoom(19).target(newLatLng).build();
        mapObject.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 18));
        mapObject.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        //mapObject.addMarker(new MarkerOptions().position(newLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).snippet("Current Location"));
        speedTextView.setText(Double.toString(speed) + " km/h");
        if (totalDistance > 1000) {
            //Experimental code here
            double kilos = totalDistance / 1000;
            kilos = Double.valueOf(format.format(kilos));
            distanceTextView.setText(kilos + " km");
        } else {
            distanceTextView.setText(totalDistance +" m");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RunningStartRunButton:
                runStarted = true;
                timesString[0] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                if (t == 1) {
                    runStarted = true;
                    runPaused = false;
                    toolbarTextView.setText("Run In Progress");
                    startButton.setText("Pause");
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer, 0);
                    t = 0;
                } else {
                    runPaused = true;
                    toolbarTextView.setText("Run Paused");
                    runStarted = false;
                    startButton.setText("Start");
                    startButton.setTextColor(Color.RED);
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    t = 1;
                }
                break;
            case R.id.RunningEndRunButton:
                stopLocationUpdates();
                DataContainer.getDataContainerInstance().setRunnerSpeedList(recordedSpeeds);
                DataContainer.getDataContainerInstance().setTimesString(timesString);
                DataContainer.getDataContainerInstance().setRunnerTrackList(runnerLocations);
                DataContainer.getDataContainerInstance().setRouteList(locationsList);
                DataContainer.getDataContainerInstance().setCaloriesBurnt(totalCalories);
                DataContainer.getDataContainerInstance().setTimeElapsed(timeTextView.getText().toString());
                timesString[1] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                toolbarTextView.setText("Run Completed");
                runCompleted = true;
                runStarted = false;
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;
                t = 1;
                secs = 0;
                mins = 0;
                milliseconds = 0;
                startButton.setText("Start");
                handler.removeCallbacks(updateTimer);
                timeTextView.setText("00:00:00");
                speedTextView.setText("0.0");
                totalDistance = 0.0;
                caloriesTextView.setText("0.0");
                distanceTextView.setText("0.0");
                //Load the results fragment
                FreeRunResultsFragment fragment = new FreeRunResultsFragment();
                FragmentManager mgr = getFragmentManager();
                FragmentTransaction transaction = mgr.beginTransaction();
                transaction.replace(R.id.MainActivityContentArea, fragment, "FreeRunResultsFragment");
                transaction.commit();
                break;
        }
    }

    @Override
    public void onPause() {
        runningMapView.onPause();
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        runningMapView.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        runningMapView.onDestroy();
        super.onDestroy();
    }

    private void calculateCurrentCaloriesBurnt(double heightFromSea, double speed)
    {   //Using my current weight as the testing weight...
        //Because we do not have a heart monitor, we will need to use approximate values
        DecimalFormat numberFormat = new DecimalFormat("#.##");
        numberFormat.setRoundingMode(RoundingMode.CEILING);
        double respiratoryExchangeRateWalking = 5.00;
        double respiratoryExchangeRateRunning = 4.86;
        double vo2max = 0.0;
        double gradient = heightFromSea / totalDistance; //fractional grade (assumed to be incline)
        double caloriesBurnt = 0.0;

        if (speed > 4) {
            vo2max = (0.2 * (speed / 3.6))  + 3.5;
            caloriesBurnt = respiratoryExchangeRateRunning * 80 * (vo2max / 1000);
            caloriesBurnt = Double.valueOf(numberFormat.format(caloriesBurnt));
            totalCalories += caloriesBurnt;
            totalCalories = Double.valueOf(numberFormat.format(totalCalories));
            caloriesTextView.setText(totalCalories +" kcal");
        } else {
            if (speed == 0.0) {
                vo2max = (0.1 * 0) + 3.5;
            } else {
                vo2max = (0.1 * (speed / 3.6)) + 3.5;
            }
            caloriesBurnt = respiratoryExchangeRateWalking * 80 * (vo2max / 1000);
            caloriesBurnt = Double.valueOf(numberFormat.format(caloriesBurnt));
            totalCalories = totalCalories + caloriesBurnt;
            totalCalories = Double.valueOf(numberFormat.format(totalCalories));
            caloriesTextView.setText(totalCalories +" kcal");
        }
    }

    public Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedTime / 1000);
            mins = (secs) / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedTime % 1000);
            timeTextView.setText("" + mins +":" + String.format("%02d", secs) + ":" +String.format("%03d", milliseconds));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, currentValues, 0, event.values.length);
            if ((Math.abs(currentValues[0] - previousValues[0]) > movementThreshold) || (Math.abs(currentValues[1] - previousValues[1]) > movementThreshold) || (Math.abs(currentValues[2] - previousValues[2]) > movementThreshold))
            {
                isMoving = true;
            } else {
                isMoving = false;
            }
            System.arraycopy(currentValues, 0, previousValues, 0, currentValues.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
