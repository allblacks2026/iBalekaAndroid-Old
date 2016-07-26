package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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
import allblacks.com.iBaleka.MainActivity;

/**
 * Created by Okuhle on 7/11/2016.
 */
public class RegistrationBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentContext;
    private AlertDialog.Builder messageBox;
    private ExecutionMode actionMode;
    private ProgressDialog progressDialog;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private UserCredential user;
    private SharedPreferences globalPreferences;
    private SharedPreferences.Editor globalEditor;

    public RegistrationBackgroundTask(Activity currentActivity){
        this.currentContext = currentActivity;
        progressDialog = new ProgressDialog(currentContext);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        globalEditor = globalPreferences.edit();
    }

    public void setUser(UserCredential user) {
        this.user = user;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentContext);
        progressDialog.setTitle("Registration in Progress");
        progressDialog.setMessage("Please wait while we process your registration");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            if (progressDialog.isShowing())
            {
                progressDialog.cancel();
            }
            if (!s.equals(null)) {
                switch (s)
                {
                    case "nullError200":
                        displayMessage("Registration Error", "The email address you tried registering with already exists. Please try again");
                        break;
                    case "nullError300":
                        displayMessage("Registration Error", "The username you tried registering has already been taken. Please try another username");
                        break;
                    case "nullError400":
                        displayMessage("Registration Error", "There was an unexpected error with registration. Please try again");
                        break;
                    case "nullSuccess":
                        displayMessage("Registration Success", "Congratulations "+user.getName()+", you have successfully registered for iBaleka, as an Athlete!");

                        globalEditor.putString("Name", user.getName());
                        globalEditor.putString("Surname", user.getSurname());
                        globalEditor.putString("EmailAddress", user.getEmailAddress());
                        globalEditor.putString("DateOfBirth", user.getDateOfBirth());
                        globalEditor.putString("Username", user.getUsername());
                        globalEditor.putString("Password", user.getPassword());
                        globalEditor.putString("SecurityQuestion", user.getSecurityQuestion());
                        globalEditor.putString("SecurityAnswer", user.getSecurityAnswer());
                        globalEditor.putString("Country", user.getCountry());
                        globalEditor.putString("UserType", "A");
                        globalEditor.commit();

                        Intent mainActivity = new Intent(currentContext, MainActivity.class);
                        currentContext.startActivity(mainActivity);
                        currentContext.finish();
                }
            }
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            displayMessage("Error Finalizing Registration", error.getMessage());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String response = null;
            String line = null;

            String registerUrl = baseUrl + "register_athlete.php";
            String enteredName = params[0];
            String enteredSurname = params[1];
            String enteredEmail = params[2];
            String enteredUsername = params[3];
            String enteredPassword = params[4];
            String securityQuestion = params[5];
            String securityAnswer = params[6];
            String dateOfBirth = params[7];
            String country = params[8];

            String registerString = URLEncoder.encode("Name", "utf-8")+"="+URLEncoder.encode
                    (enteredName, "utf-8")+"&"+URLEncoder.encode("Surname", "utf-8")
                    +"="+URLEncoder.encode(enteredSurname, "utf-8")+"&"+URLEncoder.encode("Country", "utf-8")+"="+URLEncoder.encode(country, "utf-8") +"&"+URLEncoder.encode("DateOfBirth", "utf-8")+"="+URLEncoder.encode(dateOfBirth, "utf-8")+"&"+URLEncoder.encode
                    ("EmailAddress", "utf-8")+"="+URLEncoder.encode(enteredEmail, "utf-8")
                    +"&"+URLEncoder.encode("Username", "utf-8")+"="+URLEncoder.encode
                    (enteredUsername, "utf-8")+"&"+URLEncoder.encode("Password", "utf-8")
                    +"="+URLEncoder.encode(enteredPassword, "utf-8")+"&"+URLEncoder.encode
                    ("SecurityQuestion", "utf-8")+"="+URLEncoder.encode(securityQuestion,
                    "utf-8")+"&"+URLEncoder.encode("SecurityAnswer", "utf-8")+"="+URLEncoder
                    .encode(securityAnswer, "utf-8");

            URL registerLink = new URL(registerUrl);
            HttpURLConnection registerConnection = (HttpURLConnection) registerLink
                    .openConnection();
            registerConnection.setRequestMethod("POST");
            registerConnection.setDoOutput(true);
            registerConnection.setDoInput(true);
            //Send data to the server
            OutputStream toServerStream = registerConnection.getOutputStream();
            BufferedWriter toServerWriter = new BufferedWriter(new OutputStreamWriter
                    (toServerStream, "utf-8"));
            toServerWriter.write(registerString);
            toServerWriter.flush();
            toServerWriter.close();
            //Read response from the server
            InputStream fromServerStream = registerConnection.getInputStream();
            BufferedReader fromServerReader = new BufferedReader(new InputStreamReader
                    (fromServerStream, "iso-8859-1"));
            while ((line = fromServerReader.readLine()) != null) {
                response = response + line;
            }
            fromServerReader.close();
            registerConnection.disconnect();

            return response;
        } catch (Exception error) {
            displayMessage("Error Registering", error.getMessage());
            return null;
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
