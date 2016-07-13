package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
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

import AppConstants.ExecutionMode;
import Models.UserCredential;
import allblacks.com.Activities.MainActivity;

/**
 * Created by Okuhle on 7/11/2016.
 */
public class LoginBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentContext;
    private ProgressDialog progressDialog;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private SharedPreferences globalPreferences;
    private SharedPreferences.Editor globalEditor;

    public LoginBackgroundTask(Activity currentContext) {
        this.currentContext = currentContext;
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        globalEditor = globalPreferences.edit();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentContext);
        progressDialog.setTitle("Login Action");
        progressDialog.setMessage("Please wait while we process your login request");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String response = "";
            String line = "";

            String loginUrl = baseUrl + "login_athlete.php";
            String enteredUsername = params[0];
            String enteredPassword = params[1];

            URL loginLink = new URL(loginUrl);
            HttpURLConnection loginConnection = (HttpURLConnection) loginLink.openConnection();
            loginConnection.setRequestMethod("POST");
            loginConnection.setDoInput(true);
            loginConnection.setDoOutput(true);
            //Write data to the server
            String loginString = URLEncoder.encode("Username", "utf-8")+"="+URLEncoder.encode
                    (enteredUsername, "utf-8")+"&"+URLEncoder.encode("Password", "utf-8")
                    +"="+URLEncoder.encode(enteredPassword, "utf-8");
            OutputStream toServerStream = loginConnection.getOutputStream();
            BufferedWriter toServerWriter = new BufferedWriter(new OutputStreamWriter
                    (toServerStream, "utf-8"));
            toServerWriter.write(loginString);
            toServerWriter.flush();
            toServerWriter.close();
            //Read response from the server
            InputStream fromServerStream = loginConnection.getInputStream();
            BufferedReader fromServerReader = new BufferedReader(new InputStreamReader
                    (fromServerStream, "iso-8859-1"));
            while ((line = fromServerReader.readLine()) != null) {
                response = response + line;
            }
            fromServerReader.close();
            loginConnection.disconnect();
            return response;
        } catch (final Exception error) {
            currentContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayMessage("Error Processing Login", error.getMessage());
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
                if (s.equalsIgnoreCase("Error100"))
                {
                    displayMessage("Incorrect Username / Password", "You have entered an invalid username and/or password. Please check and try again");
                } else if (s.equalsIgnoreCase("Error200")) {
                    displayMessage("Login Errors", "Please enter your login credentials before continuing");
                } else {
                    JSONObject userObj = new JSONObject(s);
                    globalEditor.putString("Name", userObj.getString("Name"));
                    globalEditor.putString("Surname", userObj.getString("Surname"));
                    globalEditor.putString("EmailAddress", userObj.getString("EmailAddress"));
                    globalEditor.putString("DateOfBirth", userObj.getString("DateOfBirth"));
                    globalEditor.putString("UserType", userObj.getString("UserType"));
                    globalEditor.putString("Username", userObj.getString("Username"));
                    globalEditor.putString("Password", userObj.getString("Password"));
                    globalEditor.putString("SecurityQuestion", userObj.getString("SecurityQuestion"));
                    globalEditor.putString("SecurityAnswer", userObj.getString("SecurityAnswer"));
                    globalEditor.putString("Country", userObj.getString("Country"));
                    globalEditor.commit();

                    Intent mainActivity = new Intent(currentContext, MainActivity.class);
                    currentContext.startActivity(mainActivity);
                    currentContext.finish();
                }
            }
        } catch (Exception error) {
            displayMessage("Error Finalizing Login", error.getMessage());
        }
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentContext);
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
