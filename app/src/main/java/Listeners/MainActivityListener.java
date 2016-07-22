package Listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import AppConstants.ExecutionMode;
import BackgroundTasks.ApplicationBackgroundTask;
import BackgroundTasks.UpdateProfileBackgroundTask;
import Utilities.TextSanitizer;
import allblacks.com.Activities.R;

/**
 * Created by Okuhle on 6/26/2016.
 */
public class MainActivityListener implements View.OnClickListener {

    private Activity currentActivity;
    private TextView mainActivityText;
    private String [] genderList = new String[] {"None Selected", "Male", "Female"};
    private SharedPreferences activityPreferences;
    private SharedPreferences.Editor editor;

    public MainActivityListener(Activity currentActivity) {
        this.currentActivity = currentActivity;
        mainActivityText = (TextView) currentActivity.findViewById(R.id.MainActivityTextView);
        activityPreferences = this.currentActivity.getSharedPreferences("iBaleka_Search", Context.MODE_PRIVATE);
        editor = activityPreferences.edit();
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()) {
           case R.id.searchEvents:
               processSearch();
               break;
           case R.id.UpdateProfileButton:
                processUpdateProfile();
               break;
       }
    }

    private void processSearch()
    {
        TextView searchParams = (TextView) currentActivity.findViewById(R.id.SearchCriteriaEditText);
        CheckBox sortByDateCheckBox = (CheckBox) currentActivity.findViewById(R.id.SortByDateCheckBox);
        String searchParameters = TextSanitizer.sanitizeText(searchParams.getText().toString().trim(), true);
        boolean sortByDate = sortByDateCheckBox.isChecked();

        if (searchParameters != null || searchParameters.length() != 0){

            String searchParam = TextSanitizer.sanitizeText(searchParameters, true);
            editor.putString("SearchCriteria", searchParam);
            editor.putBoolean("SortByDate", sortByDate);
            editor.commit();

            final TabLayout tabLayout = (TabLayout) currentActivity.findViewById(R.id.SearchTabLayout);
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tabLayout.getTabAt(1).select();
                }
            });

        } else {
            displayMessage("Search Parameters Required", "Please enter a valid search criteria");
        }
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        messageBox.show();
    }

    private void processUpdateProfile()
    {
        Double weight = null;
        Double height = null;
        String licenseNo = null;
        String gender = null;

        EditText nameEditText = (EditText) currentActivity.findViewById(R.id.EditProfileNameEditText);
        EditText surnameEditText = (EditText) currentActivity.findViewById(R.id.EditProfileSurnameEditText);
        EditText emailEditText = (EditText) currentActivity.findViewById(R.id.EditProfileEmailEditText);
        EditText passwordEditText = (EditText) currentActivity.findViewById(R.id.EditProfilePasswordEditText);
        EditText weightEditText = (EditText) currentActivity.findViewById(R.id.WeightEditText);
        EditText heightEditText = (EditText) currentActivity.findViewById(R.id.HeightEditText);
        EditText licenseNoEditText = (EditText) currentActivity.findViewById(R.id.LicenseNumberEditText);
        MaterialSpinner selectedGender = (MaterialSpinner) currentActivity.findViewById(R.id.GenderSpinner);

        int genderIndex = selectedGender.getSelectedIndex();
        if (genderIndex != 0) {
            gender = genderList[genderIndex];
        }
        String enteredName = nameEditText.getText().toString().trim();
        String enteredSurname = surnameEditText.getText().toString().trim();
        String enteredEmail = emailEditText.getText().toString().trim();
        String enteredPassword = passwordEditText.getText().toString().trim();
        if (weightEditText.getText().toString() != "") {
            weight = Double.parseDouble(weightEditText.getText().toString());
        }
        if (heightEditText.getText().toString() != "") {
            height = Double.parseDouble(heightEditText.getText().toString());
        }
        if (licenseNoEditText.getText().toString() != "") {
            licenseNo = licenseNoEditText.getText().toString();
        }

        enteredName = TextSanitizer.sanitizeText(enteredName, true);
        enteredSurname = TextSanitizer.sanitizeText(enteredSurname, true);
        enteredEmail = TextSanitizer.sanitizeText(enteredEmail, true);
        enteredPassword = TextSanitizer.sanitizeText(enteredPassword, false);


        boolean [] isValid = new boolean[4];
        isValid[0] = TextSanitizer.isValidText(enteredName, 1, 100);
        isValid[1] = TextSanitizer.isValidText(enteredSurname, 1, 100);
        isValid[2] = TextSanitizer.isValidText(enteredEmail, 1, 100);
        isValid[3] = TextSanitizer.isValidText(enteredPassword, 3, 100);

        if (isValid[0] && isValid[1] && isValid[2] && isValid[3]) {

            UpdateProfileBackgroundTask updateProfileBackgroundTask = new UpdateProfileBackgroundTask(currentActivity);
            updateProfileBackgroundTask.setExecutionMode(ExecutionMode.EXECUTE_UPDATE_ATHLETE_PROFILE);
            updateProfileBackgroundTask.execute(enteredName, enteredSurname, enteredEmail, enteredPassword, Double.toString(weight), Double.toString(height), licenseNo, gender);

        } else {

            if (!isValid[0]) {
                displayMessage("Name Required", "In order to process an update, you need to provide your name");
            } else if (!isValid[1]) {
                displayMessage("Surname Required", "In order to provide an update, you need to provide your surname");
            } else if (!isValid[2]) {
                displayMessage("Email Required", "In order to provide an update, you need to provide an email address");
            } else if (!isValid[3]) {
                displayMessage("Password Required", "In order to provide an update, the password must be valid (greater than 5 characters");
            }
        }


    }
}
