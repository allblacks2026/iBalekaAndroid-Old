package Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import BackgroundTasks.GetAthleteProfileBackgroundTask;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 3/28/2016.
 */
public class UserProfileTabFragment extends Fragment {

    private SharedPreferences appSharedPreferences;
    private GetAthleteProfileBackgroundTask backgroundTask;
    private TextView dateRegistered;
    private TextView toolbarTextView;
    private TextView totalPersonalRunsTextView;
    private TextView totalEventRunsTextView;
    private TextView weightTextView;
    private TextView heightTextView;
    private TextView nameSurnameTextView;
    private TextView userTypeTextView;
    private TextView userCountryTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.user_profile_tab_layout, container, false);
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        backgroundTask = new GetAthleteProfileBackgroundTask(getActivity());
        backgroundTask.execute(appSharedPreferences.getString("EmailAddress", ""));
        initializeComponents(myView);
        setupData();
        return myView;
    }

    private void initializeComponents(View currentView) {
        dateRegistered = (TextView) currentView.findViewById(R.id.dateRegisteredTextView);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("View Your Profile");
        totalPersonalRunsTextView = (TextView) currentView.findViewById(R.id.totalPersonalRunsTextView);
        totalEventRunsTextView = (TextView) currentView.findViewById(R.id.totalEventRunsTextView);
        weightTextView = (TextView) currentView.findViewById(R.id.weightText);
        heightTextView = (TextView) currentView.findViewById(R.id.heightText);
        nameSurnameTextView = (TextView) currentView.findViewById(R.id.profileNameSurnameTextView);
        userTypeTextView = (TextView) currentView.findViewById(R.id.userTypeTextView);
        userCountryTextView = (TextView) currentView.findViewById(R.id.countryTextView);

    }

    private void setupData() {
        weightTextView.setText(appSharedPreferences.getString("Weight", ""));
        heightTextView.setText(appSharedPreferences.getString("Height", ""));
        nameSurnameTextView.setText(appSharedPreferences.getString("Name", "") + " "+appSharedPreferences.getString("Surname", ""));
        dateRegistered.setText(appSharedPreferences.getString("DateRegistered", ""));
        totalPersonalRunsTextView.setText(appSharedPreferences.getString("TotalPersonalRuns", ""));
        totalEventRunsTextView.setText(appSharedPreferences.getString("TotalEventRuns", ""));
        userTypeTextView.setText(appSharedPreferences.getString("UserType", ""));
        userCountryTextView.setText(appSharedPreferences.getString("Country", ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("View Your Profile");
        setupData();
    }
}
