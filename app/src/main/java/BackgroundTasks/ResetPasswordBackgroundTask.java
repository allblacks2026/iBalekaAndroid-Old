package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import Fragments.LoginFragment;
import allblacks.com.Activities.R;

/**
 * Created by Okuhle on 7/14/2016.
 */
public class ResetPasswordBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentActivity;
    private SharedPreferences resetPasswordPreferences;
    private ProgressDialog progressDialog;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    public ResetPasswordBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        resetPasswordPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Resetting Your Password");
        progressDialog.setMessage("Please wait while we reset your password");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String script = "reset_password.php";
            String link = baseUrl + script;
            URL resetUrl = new URL(link);
            String line = "";
            String response = "";

            String emailAddress = params[0];
            String securityAnswer = params[1];
            String newPassword = params[2];
            String linkConnection = URLEncoder.encode("EmailAddress", "utf-8")+"="+URLEncoder.encode(emailAddress, "utf-8")+"&"+URLEncoder.encode("SecurityAnswer", "utf-8")+"="+URLEncoder.encode(securityAnswer, "utf-8")+"&"+URLEncoder.encode("NewPassword", "utf-8")+"="+URLEncoder.encode(newPassword, "utf-8");

            HttpURLConnection resetConnection = (HttpURLConnection) resetUrl.openConnection();
            resetConnection.setRequestMethod("POST");
            resetConnection.setDoInput(true);
            resetConnection.setDoOutput(true);
            OutputStream toServerStream = resetConnection.getOutputStream();
            BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "utf-8"));
            toServer.write(linkConnection);
            toServer.flush();
            toServer.close();
            InputStream fromServerStream = resetConnection.getInputStream();
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "iso-8859-1"));
            while ((line = fromServer.readLine()) != null) {
                response = response + line;
            }

            fromServer.close();
            resetConnection.disconnect();
            return response;


        } catch (final Exception error) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayMessage("Error Processing Reset Password", error.getMessage());
                }
            });
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
       try {
           if (!s.equals(null)) {
               if (s.equalsIgnoreCase("PartialSuccess")) {
                   displayMessageAndExit("Password Change Successful", "Congratulations, you have successfully changed your password");
               } else if (s.equalsIgnoreCase("Success")) {
                   displayMessageAndExit("Password Change Successful", "Congratulations, you have successfully changed your password");

               } else if (s.equalsIgnoreCase("Error100")) {
                   displayMessage("Error Resetting Password", "Please ensure you have sent a valid email address");
               } else if (s.equalsIgnoreCase("Error200")) {
                   displayMessage("Change Password Failed", "The security answer you have entered is not valid. Please try again");
               } else if (s.equalsIgnoreCase("Error300")) {
                   displayMessage("Error Updating Password", "Please note updates only happen if the password has been changed");
               }
           }
       } catch (final Exception error) {
           displayMessage("Error Finalizing Reset Password", error.getMessage());
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

    private void displayMessageAndExit(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadLoginScreen();
            }
        });
        messageBox.show();
    }

    private void loadLoginScreen()
    {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager loginMgr = currentActivity.getFragmentManager();
        FragmentTransaction transaction = loginMgr.beginTransaction();
        transaction.replace(R.id.LoginActivityContentArea, loginFragment, "LoginFragment");
        transaction.commit();

    }
}
