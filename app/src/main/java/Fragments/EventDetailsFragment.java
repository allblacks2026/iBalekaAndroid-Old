package Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {

    private SharedPreferences eventPreferences;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView locationTextView;
    private Button registerEventButton;

    public EventDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        eventPreferences = getActivity().getSharedPreferences("EventPreferences", Context.MODE_PRIVATE);
        View currentView = inflater.inflate(R.layout.fragment_event_details, container, false);
        initializeComponents(currentView);
        setEventDetails();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        titleTextView = (TextView) currentView.findViewById(R.id.EventDetailsTitleTextView);
        descriptionTextView = (TextView) currentView.findViewById(R.id.EventDetailsDescriptionTextView);
        dateTextView = (TextView) currentView.findViewById(R.id.EventDetailsDateTextView);
        timeTextView = (TextView) currentView.findViewById(R.id.EventDetailsTimeTextView);
        locationTextView = (TextView) currentView.findViewById(R.id.EventDetailsLocationTextView);
        registerEventButton = (Button) currentView.findViewById(R.id.EventDetailsRegisterForEvent);
    }

    private void setEventDetails()
    {
        titleTextView.setText(eventPreferences.getString("EventDescription", ""));
        descriptionTextView.setText(eventPreferences.getString("EventDescription", ""));
        dateTextView.setText(eventPreferences.getString("EventDate", ""));
        timeTextView.setText(eventPreferences.getString("EventTime", ""));
        locationTextView.setText(eventPreferences.getString("EventLocation", ""));
    }

}
