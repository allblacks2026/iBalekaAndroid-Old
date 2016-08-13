package Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

import Listeners.MainActivityListener;
import Models.Checkpoint;
import Utilities.DataContainer;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment implements OnMapReadyCallback {

    private SharedPreferences eventPreferences;
    private TextView eventTitleTextView;
    private TextView eventLocationTextView;
    private TextView eventTimeTextView;
    private TextView eventDistanceTextView;
    private TextView eventConditionsTextView;
    private TextView startPointTextView;
    private TextView endPointTextView;
    private TextView eventDateTextView;

    private Button registerEventButton;
    private TextView toolbarTextView;
    private MapView eventMapView;
    private GoogleMap mapObject;
    private List<Checkpoint> checkpointList;
    private List<Marker> markersList = new ArrayList<>();
    private MainActivityListener listener;

    public EventDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        eventPreferences = getActivity().getSharedPreferences("EventPreferences", Context.MODE_PRIVATE);
        checkpointList = DataContainer.getDataContainerInstance().getCheckpointList();
        View currentView = inflater.inflate(R.layout.fragment_event_details, container, false);
        initializeComponents(currentView, savedInstanceState);
        setEventDetails();
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        listener = new MainActivityListener(getActivity());
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        eventTitleTextView = (TextView) currentView.findViewById(R.id.EventDetails_EventNameLabel);
        eventTimeTextView = (TextView) currentView.findViewById(R.id.EventDetailsTimeLabel);
        eventLocationTextView = (TextView) currentView.findViewById(R.id.EventDetails_LocationLabel);
        eventDistanceTextView = (TextView) currentView.findViewById(R.id.EventDetailsDistanceLabel);
        startPointTextView = (TextView) currentView.findViewById(R.id.EventDetailsStartPointLabel);
        endPointTextView = (TextView) currentView.findViewById(R.id.EventDetailsEndPointLabel);
        eventConditionsTextView = (TextView) currentView.findViewById(R.id.EventDetailsConditionLabel);
        registerEventButton = (Button) currentView.findViewById(R.id.EventDetailsRegisterForEvent);
        eventDateTextView = (TextView) currentView.findViewById(R.id.EventDetails_DateLabel);
        registerEventButton.setOnClickListener(listener);
        eventMapView = (MapView) currentView.findViewById(R.id.EventDetailsMappedRoute);
        eventMapView.onCreate(savedInstanceState);
        eventMapView.getMapAsync(this);
    }

    private void setEventDetails()
    {
        toolbarTextView.setText(eventPreferences.getString("EventDescription", ""));
        eventTitleTextView.setText(eventPreferences.getString("EventDescription", ""));
        eventTimeTextView.setText(eventPreferences.getString("EventTime", ""));
        eventLocationTextView.setText(eventPreferences.getString("EventLocation", ""));
        eventDistanceTextView.setText(eventPreferences.getString("EventDistance", "N/A"));
        startPointTextView.setText(eventPreferences.getString("EventStartPoint", "N/A"));
        endPointTextView.setText(eventPreferences.getString("EventEndPoint", "N/A"));
        eventConditionsTextView.setText(eventPreferences.getString("EventCondition", "N/A"));
        eventDateTextView.setText(eventPreferences.getString("EventDate", "N/A"));
    }

    @Override
    public void onResume() {
        super.onResume();
        eventMapView.onResume();
        toolbarTextView.setText(eventPreferences.getString("EventDescription", ""));
    }

    @Override
    public void onPause() {
        eventMapView.onPause();
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapObject = googleMap;
        mapObject.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings mapSettings = mapObject.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setZoomGesturesEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setMapToolbarEnabled(true);
        if (checkpointList.size() != 0) {
            mapObject.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(checkpointList.get(0).getLatitude()), Double.parseDouble(checkpointList.get(0).getLongitude())), 14));
            addPointsToMap();
        }
    }

    private void addPointsToMap()
    {

        PolygonOptions lineOptions = new PolygonOptions();
        for ( int a = 0; a < checkpointList.size(); a++) {
            Checkpoint point = checkpointList.get(a);
            if (a == 0) {
                lineOptions.add(new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude())));
                mapObject.addMarker(new MarkerOptions().title("Start Point").position(new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude()))));
            }
            else if (a == checkpointList.size() -1) {
                lineOptions.add(new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude())));
                mapObject.addMarker(new MarkerOptions().title("End Point").position(new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude()))));
            }
            else {
                lineOptions.add(new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude())));
            }
        }
        Polygon polygon = mapObject.addPolygon(lineOptions);
        polygon.setStrokeColor(Color.BLUE);
        polygon.setFillColor(Color.BLUE);

    }

    @Override
    public void onDestroy() {
        mapObject.clear();
        super.onDestroy();
    }
}
