package Listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;

import BackgroundTasks.ForgotPasswordBackgroundTask;
import BackgroundTasks.ResetPasswordBackgroundTask;
import Utilities.TextSanitizer;
import allblacks.com.Activities.R;

/**
 * Created by Okuhle on 7/13/2016.
 */
public class ForgotPasswordListener implements View.OnClickListener {

    private Activity currentActivity;
    private SharedPreferences appSharedPreferences;

    public ForgotPasswordListener(Activity currentActivity) {
        this.currentActivity = currentActivity;
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RetrievePasswordButton:
                EditText securityAnswerEditText = (EditText) currentActivity.findViewById(R.id.ForgotPasswordSecurityAnswerEditText);
                EditText newPasswordEditText = (EditText) currentActivity.findViewById(R.id.ForgotPasswordNewPasswordEditText);
                EditText confirmPasswordEditText = (EditText) currentActivity.findViewById(R.id.ForgotPasswordConfirmNewPasswordEditText);

                String answer = securityAnswerEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                answer = TextSanitizer.sanitizeText(answer, true);
                newPassword = TextSanitizer.sanitizeText(newPassword, false);
                confirmPassword = TextSanitizer.sanitizeText(confirmPassword, false);

                boolean isValid [] = new boolean[4];
                isValid[0] = TextSanitizer.isValidText(answer, 2, 100);
                isValid[1] = TextSanitizer.isValidText(newPassword, 2, 100);
                isValid[2] = TextSanitizer.isValidText(confirmPassword, 2, 100);
                boolean isSame = TextSanitizer.isSameText(newPassword, confirmPassword);
                if (isValid[0] && isValid[1] && isValid[2] && isSame) {
                    ResetPasswordBackgroundTask backgroundTask = new ResetPasswordBackgroundTask(currentActivity);
                    backgroundTask.execute(appSharedPreferences.getString("EmailAddress", ""), answer, newPassword);

                } else {
                    if (!isSame) {
                        displayMessage("Error in New Password", "The entered passwords do not match");
                    } else {
                        displayMessage("Error Sending Change Request", "Please ensure you have filled all fields with valid data");
                    }
                }


                break;
        }
    }

    private void displayMessage(String title, String message) {
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
