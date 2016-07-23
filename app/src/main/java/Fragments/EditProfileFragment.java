package Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import AppConstants.ExecutionMode;
import BackgroundTasks.UpdateProfileBackgroundTask;
import Listeners.MainActivityListener;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText licenseNoEditText;
    private EditText securityQuestionEditText;
    private EditText securityAnswerEditText;
    private MaterialSpinner genderSpinner;
    private Button updateButton;
    private List<String> genders = new ArrayList<String>();
    private UpdateProfileBackgroundTask updateProfileBackgroundTask;
    private SharedPreferences appSharedPreferences;
    private MainActivityListener buttonListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView =  inflater.inflate(R.layout.fragment_edit_profile, container, false);
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initalizeComponents(currentView);
        updateProfileBackgroundTask = new UpdateProfileBackgroundTask(getActivity());
        updateProfileBackgroundTask.setTextBoxes(nameEditText, surnameEditText, emailEditText, passwordEditText, weightEditText, heightEditText, licenseNoEditText, genderSpinner, securityQuestionEditText, securityAnswerEditText);
        updateProfileBackgroundTask.setExecutionMode(ExecutionMode.EXECUTE_GET_ATHLETE_PROFILE);
        updateProfileBackgroundTask.execute(appSharedPreferences.getString("EmailAddress", ""));
        return currentView;
    }

    private void initalizeComponents(View currentView) {
        genders.add("None Selected");
        genders.add("Male");
        genders.add("Female");
        nameEditText = (EditText) currentView.findViewById(R.id.EditProfileNameEditText);
        surnameEditText = (EditText) currentView.findViewById(R.id.EditProfileSurnameEditText);
        emailEditText = (EditText) currentView.findViewById(R.id.EditProfileEmailEditText);
        emailEditText.setKeyListener(null);
        securityQuestionEditText = (EditText) currentView.findViewById(R.id.EditProfileSecurityQuestionEditText);
        securityAnswerEditText = (EditText) currentView.findViewById(R.id.EditProfileSecurityAnswerEditText);
        passwordEditText = (EditText) currentView.findViewById(R.id.EditProfilePasswordEditText);
        weightEditText = (EditText) currentView.findViewById(R.id.WeightEditText);
        heightEditText = (EditText) currentView.findViewById(R.id.HeightEditText);
        licenseNoEditText = (EditText) currentView.findViewById(R.id.LicenseNumberEditText);
        genderSpinner = (MaterialSpinner) currentView.findViewById(R.id.GenderSpinner);
        genderSpinner.setItems(genders);
        buttonListener = new MainActivityListener(getActivity());
        updateButton = (Button) currentView.findViewById(R.id.UpdateProfileButton);
        updateButton.setOnClickListener(buttonListener);

    }

}
