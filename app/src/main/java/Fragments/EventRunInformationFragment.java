package Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventRunInformationFragment extends Fragment {


    public EventRunInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_run_information, container, false);
    }

}
