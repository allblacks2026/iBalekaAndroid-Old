package Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationPreferencesFragment extends PreferenceFragment {

    private boolean rememberCredentials;
    private String measurementMode;
    private SharedPreferences applicationPreferences;
    private SharedPreferences fragmentPreferences;
    private SharedPreferences.Editor editor;
    public ApplicationPreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.application_preferences);
        applicationPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        fragmentPreferences = getActivity().getPreferences(Context.MODE_APPEND);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.application_preferences, false);
        editor = fragmentPreferences.edit();
    }

    @Override
    public void onResume() {
        super.onResume();
        rememberCredentials = applicationPreferences.getBoolean("remember_creditals", false);
        measurementMode = applicationPreferences.getString("measurement_setting", "MODE_METRIC");

        if (!rememberCredentials) {
            //remove the username and password
        }
    }

    @Override
    public void onPause() {
        editor.commit();
        super.onPause();
    }
}
