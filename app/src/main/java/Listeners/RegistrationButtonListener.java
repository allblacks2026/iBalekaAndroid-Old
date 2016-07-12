package Listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AppConstants.ExecutionMode;
import BackgroundTasks.RegistrationBackgroundTask;
import BackgroundTasks.UserGatewayBackgroundTask;
import Fragments.CreateAccountStepTwoFragment;
import Models.User;
import Models.UserCredential;
import Utilities.TextSanitizer;
import allblacks.com.Activities.R;

/**
 * Created by Okuhle on 6/26/2016.
 */
public class RegistrationButtonListener implements View.OnClickListener {

    private Activity currentActivity;
    private SharedPreferences appSharedPreferences;
    private SharedPreferences globalPreferences;
    private SharedPreferences.Editor editor;
    private int selectedDay = 0, selectedMonth = 0, selectedYear = 0;
    private TextView selectedDOB;
    private String [] countryList;

    public RegistrationButtonListener(Activity currentActivity) {
        this.currentActivity = currentActivity;
        appSharedPreferences = currentActivity.getSharedPreferences("iBalekaRegistration", Context.MODE_PRIVATE);
        editor = appSharedPreferences.edit();
        countryList = currentActivity.getResources().getStringArray(R.array.countries_list);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SelectDateOfBirthButton:
            selectedDOB = (TextView) currentActivity.findViewById(R.id.SelectedDateOfBirthLabel);
                DatePickerDialog dateDialog = new DatePickerDialog(currentActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDay = dayOfMonth;
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;
                        selectedDOB.setText(selectedYear+"-"+selectedMonth+"-"+selectedDay);

                        editor.putString("DateOfBirth", selectedYear+"-"+selectedMonth+"-"+selectedDay);
                    }}, 2015, 10, 10);
             dateDialog.show();
                break;
            case R.id.NextStepButton:
                TextView enteredName = (TextView) currentActivity.findViewById(R.id.NameEditText);
                TextView enteredSurname = (TextView) currentActivity.findViewById(R.id.SurnameEditText);
                TextView enteredEmail = (TextView) currentActivity.findViewById(R.id.EmailEditText);
                MaterialSpinner selectedCountry = (MaterialSpinner) currentActivity.findViewById(R.id.CountrySpinner);
                int selectedCountryIndex = selectedCountry.getSelectedIndex();
                String country = countryList[selectedCountryIndex];


                if (enteredName.getText().toString().length() != 0 && enteredSurname.getText().toString().trim() != null && enteredEmail.getText().toString().trim() != null) {

                    Random randNum = new Random(5000);
                    String generatedAthleteID = Integer.toString(enteredName.length() + enteredSurname.length() + enteredEmail.length() + randNum.nextInt());
                    int athleteID = new Integer(generatedAthleteID);
                    String name = TextSanitizer.sanitizeText(enteredName.getText().toString(), true);
                    String surname = TextSanitizer.sanitizeText(enteredSurname.getText().toString(), true);
                    String email = TextSanitizer.sanitizeText(enteredEmail.getText().toString(), true);

                    boolean isValidName = TextSanitizer.isValidText(name, 1, 100);
                    boolean isValidSurname = TextSanitizer.isValidText(surname, 1, 100);
                    boolean isValidEmail = TextSanitizer.isValidEmail(email, 1, 100);

                    if (!isValidName) {
                        displayMessage("Invalid Name", "The entered name must be between 1 and 100 characters");
                    } else if (!isValidSurname) {
                        displayMessage("Invalid Email", "The entered surname must be betweeen 1 and 100 characters");
                    } else if (!isValidEmail) {
                        displayMessage("Invalid EmailAddress", "Please ensure you enter a valid email address");
                    }
                    //If these three parameters have been correctly added, save current information, and move to the next fragment
                    if (isValidName && isValidSurname && isValidEmail) {
                        editor.putInt("AthleteID", athleteID);
                        editor.putString("EnteredName", name);
                        editor.putString("EnteredSurname", surname);
                        editor.putString("EnteredEmail", email);
                        editor.putString("Country", country);
                        editor.commit();

                        CreateAccountStepTwoFragment nextStepFrag = new CreateAccountStepTwoFragment();
                        FragmentManager mgr = currentActivity.getFragmentManager();
                        FragmentTransaction transaction = mgr.beginTransaction();
                        transaction.replace(R.id.LoginActivityContentArea, nextStepFrag, "NextStepFragment");
                        transaction.addToBackStack("NextStepFragment");
                        transaction.commit();
                    } else {
                        displayMessage("Invalid Registration", "In order to continue with registration, please ensure all fields have data");
                    }
                } else {
                    displayMessage("Please Enter Text", "Please note that all fields are required");
                }
            break;
            case R.id.CreateAccountButton:

                EditText usernameEditText = (EditText) currentActivity.findViewById(R.id.UsernameEditText);
                EditText passwordEditText = (EditText) currentActivity.findViewById(R.id.PasswordEditText);
                EditText securityQuestionEditText = (EditText) currentActivity.findViewById(R.id.SecurityQuestionEditText);
                EditText securityAnswerEditText = (EditText) currentActivity.findViewById(R.id.SecurityAnswerEditText);

                if (usernameEditText.getText().toString() != null && passwordEditText.getText().toString() != null && securityQuestionEditText.getText().toString() != null && securityAnswerEditText.getText().toString() != null && selectedDay == 0 && selectedMonth == 0 && selectedYear == 0) {
                    String username = TextSanitizer.sanitizeText(usernameEditText.getText().toString(), false);
                    String password = TextSanitizer.sanitizeText(passwordEditText.getText().toString(), false);
                    String question = TextSanitizer.sanitizeText(securityQuestionEditText.getText().toString(), false);
                    String answer = TextSanitizer.sanitizeText(securityAnswerEditText.getText().toString(), false);

                    boolean [] isValid = new boolean[4];
                    isValid[0] = TextSanitizer.isValidText(username, 1, 20);
                    isValid[1] = TextSanitizer.isValidText(password, 1, 20);
                    isValid[2] = TextSanitizer.isValidText(question, 1, 150);
                    isValid[3] = TextSanitizer.isValidText(answer, 1, 150);

                    if (isValid[0] && isValid[1] && isValid[2] && isValid[3]) {
                        UserCredential user = new UserCredential(Integer.toString(appSharedPreferences.getInt("AthleteID", 0)), appSharedPreferences.getString("EnteredName", ""), appSharedPreferences.getString("EnteredSurname", ""), appSharedPreferences.getString("EnteredEmail", ""), appSharedPreferences.getString("Country", ""), "A", appSharedPreferences.getString("DateOfBirth", ""), username, password, question, answer);
                        RegistrationBackgroundTask registrationBackgroundTask = new RegistrationBackgroundTask(currentActivity);
                        registrationBackgroundTask.setUser(user);
                        registrationBackgroundTask.execute(user.getName(), user.getSurname(), user.getEmailAddress(), user.getUsername(), user.getPassword(), user.getSecurityQuestion(), user.getSecurityAnswer(), user.getDateOfBirth(), user.getCountry(), user.getUserID());


                    } else {
                        displayMessage("Invalid Data Detected", "One or more text fields contain insufficient / invalid data. Please ensure data entered is between 1 and 100 characters");
                    }
                } else {
                    displayMessage("All Fields Required", "One or more fields have missing data. Please note that all fields are required");
                }

        }
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        messageBox.show();
    }
}
