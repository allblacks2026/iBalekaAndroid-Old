package Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteLandingFragment extends Fragment {

    private SharedPreferences globalPreferences;
    public AthleteLandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_athlete_landing, container, false);
    }
    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRetainInstance();

    }
}
