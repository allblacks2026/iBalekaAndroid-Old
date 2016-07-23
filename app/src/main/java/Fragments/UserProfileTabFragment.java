package Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import BackgroundTasks.GetAthleteProfileBackgroundTask;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 3/28/2016.
 */
public class UserProfileTabFragment extends Fragment {

    private SharedPreferences appSharedPreferences;
    private GetAthleteProfileBackgroundTask backgroundTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.user_profile_tab_layout, container, false);
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        backgroundTask = new GetAthleteProfileBackgroundTask(getActivity());
        backgroundTask.execute(appSharedPreferences.getString("EmailAddress", ""));
        return myView;
    }
}
