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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import Fragments.ForgotPasswordStepTwoFragment;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 7/13/2016.
 */
public class ForgotPasswordBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentActivity;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private ProgressDialog progressDialog;
    private SharedPreferences forgotPasswordPreferences;
    private SharedPreferences.Editor editor;

    public ForgotPasswordBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        forgotPasswordPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        editor = forgotPasswordPreferences.edit();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Finding a Matching Account");
        progressDialog.setMessage("Please wait while we find a matching account with the provided email address");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String line = "";
            String response = "";
            String emailAddress = params[0];

            String link = baseUrl + "forgot_password.php";
            String Url = URLEncoder.encode("EmailAddress", "utf-8") + "=" +URLEncoder.encode(emailAddress, "utf-8");
            URL passwordUrl = new URL(link);
            HttpURLConnection passwordConnection = (HttpURLConnection) passwordUrl.openConnection();
            passwordConnection.setRequestMethod("POST");
            passwordConnection.setDoInput(true);
            passwordConnection.setDoOutput(true);
            OutputStream toServerStream = passwordConnection.getOutputStream();
            BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "utf-8"));
            toServer.write(Url);
            toServer.flush();
            toServer.close();
            InputStream fromServerSteam = passwordConnection.getInputStream();
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerSteam, "iso-8859-1"));
            while ((line = fromServer.readLine()) != null) {
                response = response + line;
            }

            return response;


        } catch (final Exception error) {
            displayMessage("Error Processing Forgot Password", error.getMessage());
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

                if (s.equalsIgnoreCase("Error200")) {
                    displayMessage("No Matching Account Found", "No matching account was found for the entered email address");
                } else if (s.equalsIgnoreCase("Error100")) {
                    displayMessage("Please Supply an E-Mail Address", "Please be sure to supply a valid email address if you want to recover your password");
                } else {
                    JSONObject userObject = new JSONObject(s);
                    editor.putString("UserID", userObject.getString("UserID"));
                    editor.putString("Name", userObject.getString("Name"));
                    editor.putString("Surname", userObject.getString("Surname"));
                    editor.putString("EmailAddress", userObject.getString("EmailAddress"));
                    editor.putString("Username", userObject.getString("Username"));
                    editor.putString("SecurityQuestion", userObject.getString("SecurityQuestion"));
                    editor.putString("SecurityAnswer", userObject.getString("SecurityAnswer"));
                    if (editor.commit()) {
                        ForgotPasswordStepTwoFragment finalStepFragment = new ForgotPasswordStepTwoFragment();
                        FragmentManager manager = currentActivity.getFragmentManager();
                        FragmentTransaction changeFragment = manager.beginTransaction();
                        changeFragment.replace(R.id.LoginActivityContentArea, finalStepFragment, "ForgotPasswordFinalStepFragment");
                        changeFragment.addToBackStack("ForgotPasswordFinalStepFragment");
                        changeFragment.commit();
                    }
                }
            }
        } catch (Exception error) {
            displayMessage("Error Finishing Forgot Password", error.getMessage());
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
}
