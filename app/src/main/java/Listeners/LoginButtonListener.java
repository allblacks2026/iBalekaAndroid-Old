package Listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import BackgroundTasks.ForgotPasswordBackgroundTask;
import BackgroundTasks.LoginBackgroundTask;
import Fragments.CreateAccountStepOneFragment;
import Fragments.ForgotPasswordFragment;
import Utilities.DeviceHardwareChecker;
import Utilities.TextSanitizer;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 5/26/2016.
 */
public class LoginButtonListener implements View.OnClickListener {

    private Activity currentContext;
    private TextView toolbarTextView;
    private FragmentManager fragmentManager;
    private LoginBackgroundTask userGatewayTask;
    private ForgotPasswordBackgroundTask forgotPasswordBackgroundTask;
    private SharedPreferences applicationPreferences;
    private SharedPreferences.Editor editor;

    private EditText forgotPasswordEmailEditText;

    public LoginButtonListener(Activity currentContext) {
        this.currentContext = currentContext;
        toolbarTextView = (TextView) currentContext.findViewById(R.id.LoginActivityToolbarTextView);
        fragmentManager = currentContext.getFragmentManager();
        applicationPreferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        editor = applicationPreferences.edit();
        forgotPasswordBackgroundTask = new ForgotPasswordBackgroundTask(currentContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                DeviceHardwareChecker checker = new DeviceHardwareChecker(currentContext);
                checker.checkNetworkConnection();
                if (checker.isConnectedToInternet()) {

                    TextView usernameEditText = (TextView) currentContext.findViewById(R.id.usernameEditText);
                    TextView passwordEditText = (TextView) currentContext.findViewById(R.id.passwordEditText);

                    if (usernameEditText.getText().toString() != null && passwordEditText.getText().toString() != null) {
                        String userName = TextSanitizer.sanitizeText(usernameEditText.getText().toString().trim(), false);
                        String password = TextSanitizer.sanitizeText(passwordEditText.getText().toString().trim(), false);

                        boolean isValidUsername = TextSanitizer.isValidText(userName, 1, 100);
                        boolean isValidPassword = TextSanitizer.isValidText(password, 1, 100);

                        if (isValidUsername && isValidPassword) {
                            userGatewayTask = new LoginBackgroundTask(currentContext);
                            userGatewayTask.execute(userName, password);
                        } else {
                            displayMessage("Login Error", "Please ensure your username and password is between 1 and 100 characters");
                        }
                    } else {
                        displayMessage("Login Error", "Please Enter a valid Username and Password");
                    }
                } else {
                    displayMessage("Check Internet Connection", "You are not connected to the internet. Please check your internet connection");
                }

                break;
            case R.id.registerAccountbtn:
                CreateAccountStepOneFragment registerAccountFragment = new
                        CreateAccountStepOneFragment();
                FragmentTransaction createAccountTransaction = fragmentManager.beginTransaction();
                createAccountTransaction.replace(R.id.LoginActivityContentArea,
                        registerAccountFragment, "RegisterFragmentStepOne");
                createAccountTransaction.addToBackStack("RegisterFragmentStepOne");
                createAccountTransaction.commit();
                toolbarTextView.setText("Register Account - Step 1 of 2");
                break;
            case R.id.forgotPasswordButton:
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                FragmentTransaction forgotPasswordTransaction = fragmentManager.beginTransaction();
                forgotPasswordTransaction.replace(R.id.LoginActivityContentArea,
                        forgotPasswordFragment, "ForgotPasswordFragment");
                toolbarTextView.setText("Reset Your Password");
                forgotPasswordTransaction.addToBackStack("ForgotPasswordFragment");
                forgotPasswordTransaction.commit();
                break;
            case R.id.ForgotPasswordNextStepButton:
                DeviceHardwareChecker checkInternet = new DeviceHardwareChecker(currentContext);
                checkInternet.checkNetworkConnection();
                if (checkInternet.isConnectedToInternet()) {
                    forgotPasswordEmailEditText = (EditText) currentContext.findViewById(R.id
                            .ForgotPasswordEmailEditText);
                    String enteredEmail = TextSanitizer.sanitizeText(forgotPasswordEmailEditText
                            .getText().toString(), true);
                    boolean isValidText = TextSanitizer.isValidText(enteredEmail, 10, 100);
                    if (isValidText) {
                        forgotPasswordBackgroundTask.execute(enteredEmail);
                    } else {
                        displayMessage("Invalid Email Entered", "Please enter an email address that " +
                                "is between 10 and 100 characters");
                    }
                } else {
                    displayMessage("Check Your Internet Connection", "You are not connected to the internet. Please check your internet connection");
                }
                break;
        }

    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentContext);
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
