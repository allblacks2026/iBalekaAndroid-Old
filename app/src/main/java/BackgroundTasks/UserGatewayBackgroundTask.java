package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import Models.User;
import Models.UserCredential;
import allblacks.com.iBaleka.MainActivity;

/**
 * Created by Okuhle on 5/6/2016.
 */
public class UserGatewayBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentContext;
    private AlertDialog.Builder messageBox;
    private ExecutionMode actionMode; //0 for log in, 1 for registration, 2 for forgot password
    private ProgressDialog progressDialog;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private UserCredential user;

    public UserGatewayBackgroundTask(Activity currentContext) {
        this.currentContext = currentContext;
        progressDialog = new ProgressDialog(currentContext);
    }

    public void setUser(UserCredential user) {
        this.user = user;
    }

    @Override
    protected void onPreExecute() {
        if (actionMode == ExecutionMode.EXECUTE_LOGIN) {
            progressDialog.setTitle("Login Action");
            progressDialog.setMessage("Please wait while we process your login request");
            progressDialog.show();
        } else if (actionMode == ExecutionMode.EXECUTE_REGISTER) {
            progressDialog.setTitle("Registration Action");
            progressDialog.setMessage("Please wait while we process your registration");
            progressDialog.show();
        } else if (actionMode == ExecutionMode.EXECUTE_FORGOT_PASSWORD) {
            progressDialog.setTitle("Forgot Password Action");
            progressDialog.setMessage("Please wait while we locate your account details");
            progressDialog.show();
        }

    }
    //0 for login, 1 for registration, 2 for forgotten password
    public void setExecutionMode(ExecutionMode mode) {
        this.actionMode = mode;
    }

    protected String doInBackground(String... params) {
        String line = null;
        String response = null;

        try {
            if (actionMode == ExecutionMode.EXECUTE_LOGIN) { //login action
                response = null;
                line = null;

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
                        (enteredUsername, "utf-8")+URLEncoder.encode("Password", "utf-8")
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
            } else if (actionMode == ExecutionMode.EXECUTE_REGISTER) { //Registration action
                response = null;
                line = null;

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
                String athleteID = params[9];

                String registerString = URLEncoder.encode("Name", "utf-8")+"="+URLEncoder.encode
                        (enteredName, "utf-8")+URLEncoder.encode("Surname", "utf-8")
                        +"="+URLEncoder.encode(enteredSurname, "utf-8")+URLEncoder.encode
                        ("EmailAddress", "utf-8")+"="+URLEncoder.encode(enteredEmail, "utf-8")
                        +URLEncoder.encode("Username", "utf-8")+"="+URLEncoder.encode
                        (enteredUsername, "utf-8")+URLEncoder.encode("Password", "utf-8")
                        +"="+URLEncoder.encode(enteredPassword, "utf-8")+URLEncoder.encode
                        ("SecurityQuestion", "utf-8")+"="+URLEncoder.encode(securityQuestion,
                        "utf-8")+URLEncoder.encode("SecurityAnswer", "utf-8")+"="+URLEncoder
                        .encode(securityAnswer, "utf-8")+URLEncoder.encode("Country", "utf-8")+"="+URLEncoder.encode(country, "utf-8") +URLEncoder.encode("DateOfBirth", "utf-8")+"="+URLEncoder.encode(dateOfBirth, "utf-8");

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
            } else if (actionMode == ExecutionMode.EXECUTE_FORGOT_PASSWORD) { //For the forgotten password

                line = null;
                response = null;
                String enteredEmailAddress = params[0];

                String forgotPasswordUrl = baseUrl + "forgot_password.php";

                URL forgotPasswordLink = new URL(forgotPasswordUrl);
                HttpURLConnection forgetPasswordConnection = (HttpURLConnection)
                        forgotPasswordLink.openConnection();
                forgetPasswordConnection.setRequestMethod("POST");
                forgetPasswordConnection.setDoOutput(true);
                forgetPasswordConnection.setDoInput(true);

                String forgotPasswordString = URLEncoder.encode("EmailAddress", "utf-8")
                        +"="+URLEncoder.encode(enteredEmailAddress, "utf-8")+URLEncoder.encode
                        ("Username", "utf-8");
                OutputStream toServerStream = forgetPasswordConnection.getOutputStream();
                BufferedWriter toServerWriter = new BufferedWriter(new OutputStreamWriter
                        (toServerStream, "UTF-8"));
                toServerWriter.write(forgotPasswordString);
                toServerWriter.flush();
                toServerWriter.close();

                InputStream fromServerStream = forgetPasswordConnection.getInputStream();
                BufferedReader fromServerReader = new BufferedReader(new InputStreamReader
                        (fromServerStream, "iso-8859-1"));
                while ((line = fromServerReader.readLine()) != null) {
                    response = response + line;
                }
                fromServerReader.close();
                forgetPasswordConnection.disconnect();
                return response;
            }


            return response;

        } catch (Exception error) {
            if (actionMode == ExecutionMode.EXECUTE_LOGIN) {
                displayMessage("Error Logging In", "Error logging athlete in "+error.getMessage());
            } else if (actionMode == ExecutionMode.EXECUTE_REGISTER) {
                displayMessage("Error Registering Athlete", "Error registering the athlete "+error
                        .getMessage());
            } else if (actionMode == ExecutionMode.EXECUTE_FORGOT_PASSWORD) {
                displayMessage("Error Getting Password", "Error getting password information "+error
                        .getMessage());
            }

            return null;
        }
    }

    //This is the after math - lets parse the json
    @Override
    protected void onPostExecute(String s) {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        try {
            if (actionMode == ExecutionMode.EXECUTE_LOGIN) {
                JSONArray loginArray = new JSONArray(s);
                JSONObject loginObject = loginArray.getJSONObject(0); //We are only getting one
                User loggedInUser = new User(loginObject.getString("UserID"), loginObject.getString("Name"), loginObject.getString("Surname"), loginObject.getString("EmailAddress"), loginObject.getString("Country"), loginObject.getString("UserType"),loginObject.getString("DateOfBirth"));

            } else if (actionMode == ExecutionMode.EXECUTE_REGISTER) {

                JSONArray registrationArray = new JSONArray(s);

                if (registrationArray.getString(0) == "error") {
                    displayMessage("Registration error", registrationArray.getJSONObject(0).getString("message"));
                } else if (registrationArray.getString(0) == "success") {
                    //Store the registration stuff into the system
                    displayMessage("Successful Registration", registrationArray.getJSONObject(0).getString("message"));
                    SharedPreferences globalPrefs = PreferenceManager.getDefaultSharedPreferences(currentContext);
                    SharedPreferences.Editor editor = globalPrefs.edit();
                    editor.putString("Name", user.getName());
                    editor.putString("Surname", user.getSurname());
                    editor.putString("EmailAddress", user.getEmailAddress());
                    editor.putString("Username", user.getUsername());
                    editor.putString("Password", user.getPassword());
                    editor.putString("SecurityQuestion", user.getSecurityQuestion());
                    editor.putString("SecurityAnswer", user.getSecurityAnswer());
                    editor.putString("Country", user.getCountry());
                    editor.putString("AthleteID", user.getUserID());
                    editor.commit();
                    //Open the new Activity
                    Intent mainActivity = new Intent(currentContext, MainActivity.class);
                    currentContext.startActivity(mainActivity);
                    currentContext.finish();
                }
            }
        } catch (Exception error) {

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
