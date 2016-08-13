package Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.text.DecimalFormat;
import java.util.List;

import Models.Checkpoint;
import Utilities.DataContainer;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeRunResultsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private TextView finishRunHeading;
    private TextView finishRunMessage;
    private MapView finishRunMapView;
    private TextView totalTime;
    private TextView averageSpeed;
    private TextView totalDistance;
    private TextView totalCaloriesBurnt;
    private TextView highestSpeed;
    private TextView toolbarTextView;
    private SharedPreferences appPreferences;
    private Button finishRunButton;

    //For the accelerometer

    public FreeRunResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_free_run_results, container, false);
        initializeComponents(currentView, savedInstanceState);
        setLabels();
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("Free Run Completed");
        finishRunHeading = (TextView) currentView.findViewById(R.id.FinishRunHeadingLabel);
        finishRunMessage = (TextView) currentView.findViewById(R.id.FinishRunShortMessage);
        finishRunMapView = (MapView) currentView.findViewById(R.id.FinishRunMapView);
        finishRunMapView.onCreate(savedInstanceState);
        totalTime = (TextView) currentView.findViewById(R.id.FinishRunTotalTimeTextView);
        averageSpeed = (TextView) currentView.findViewById(R.id.FinishRunAverageSpeedTextView);
        totalDistance = (TextView) currentView.findViewById(R.id.FinishRunTotalDistanceTextView);
        totalCaloriesBurnt = (TextView) currentView.findViewById(R.id.FinishRunTotalCaloriesBurntTextView);
        highestSpeed = (TextView) currentView.findViewById(R.id.FinishRunHighestSpeedTextView);
        finishRunButton = (Button) currentView.findViewById(R.id.FinishRunButton);
        finishRunButton.setOnClickListener(this);
        finishRunMapView.getMapAsync(this);
        appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }



    private void setLabels()
    {
        Double runnerDistance = DataContainer.getDataContainerInstance().getRunnerDistance();
        String timeTaken = DataContainer.getDataContainerInstance().getTimeElapsed();
        Double averagePace = DataContainer.getDataContainerInstance().getAverageSpeed();
        Double highestSpeedRecorded = DataContainer.getDataContainerInstance().getHighestSpeed();
        Double runnerTotalCaloriesBurnt = DataContainer.getDataContainerInstance().getCaloriesBurnt();
        DecimalFormat format = new DecimalFormat("#.##");
        averagePace = Double.valueOf(format.format(averagePace * 3.6));
        highestSpeedRecorded = Double.valueOf(format.format(highestSpeedRecorded * 3.6));
        finishRunHeading.setText("Congratulations " +appPreferences.getString("Name", ""));
        finishRunMessage.setText("You have just ran "+runnerDistance + " km at an average pace of " +averagePace+ " km/h");
        totalTime.setText(timeTaken);
        totalDistance.setText(runnerDistance + " km");
        highestSpeed.setText(highestSpeedRecorded +" km/h");
        totalCaloriesBurnt.setText(runnerTotalCaloriesBurnt + " kcal");
        averageSpeed.setText(averagePace + " km/h");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setRunnerRoute(googleMap);
    }

    @Override
    public void onClick(View v) {
        //When this button is pressed, the user must be taken back to their home landing screen
        FragmentManager mgr = getFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        transaction.replace(R.id.MainActivityContentArea, new AthleteLandingFragment(), "AthleteLandingFragment");
        transaction.commit();
        //Don't save to backstack
    }

    private void setRunnerRoute(GoogleMap map)
    {
        PolygonOptions runnerLineOptions = new PolygonOptions();
        List<Location> runnerList = DataContainer.getDataContainerInstance().getRunnerTrackList();

        Location firstLocation = runnerList.get(0);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 19));

        for (int a = 0; a < runnerList.size(); a++) {
            Location currentLocation = runnerList.get(a);
            if(a == 0) {
                runnerLineOptions.add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                map.addMarker(new MarkerOptions().title("Start Point").position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
            } else if (a == runnerList.size() - 1) {
                runnerLineOptions.add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                map.addMarker(new MarkerOptions().title("End Point").position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
            } else {
                runnerLineOptions.add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            }
        }

        Polygon runnerPolygon = map.addPolygon(runnerLineOptions);
        runnerLineOptions.strokeColor(Color.BLUE);
    }

    @Override
    public void onPause() {
        finishRunMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        finishRunMapView.onResume();
    }

    @Override
    public void onDestroy() {
        finishRunMapView.onDestroy();
        super.onDestroy();
    }
}
