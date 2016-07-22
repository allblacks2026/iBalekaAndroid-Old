package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;

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

/**
 * Created by Okuhle on 7/11/2016.
 */
public class UpdateProfileBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentActivity;
    private SharedPreferences appSharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private ExecutionMode mode;
    private String baseUrl = "http://154.127.61.157/ibaleka/";

    private EditText nameEditText, surnameEditText, emailEditText, passwordEditText, weightEditText, heightEditText, licenseNoEditText;
    private MaterialSpinner genderSpinner;

    public UpdateProfileBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        editor = appSharedPreferences.edit();
    }

    public void setTextBoxes(EditText nameEditText, EditText surnameEditText, EditText emailEditText, EditText passwordEditText, EditText weightEditText, EditText heightEditText, EditText licenseNoEditText, MaterialSpinner genderSpinner) {
        this.nameEditText = nameEditText;
        this.surnameEditText = surnameEditText;
        this.emailEditText = emailEditText;
        this.passwordEditText = passwordEditText;
        this.weightEditText = weightEditText;
        this.heightEditText = heightEditText;
        this.licenseNoEditText = licenseNoEditText;
        this.genderSpinner = genderSpinner;
    }

    public void setExecutionMode(ExecutionMode mode) {
        this.mode = mode;
    }
    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Update Profile Action");
        progressDialog.setMessage("Please wait while we fetch your existing profile information");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        switch (mode) {
            case EXECUTE_GET_ATHLETE_PROFILE:
                try {
                    String line = "";
                    String response = "";
                    String link = baseUrl + "get_athlete_profile.php";
                    URL profileUrl = new URL(link);
                    String emailAddress = params[0]; //athlete email address
                    HttpURLConnection getProfileConnection = (HttpURLConnection) profileUrl.openConnection();
                    getProfileConnection.setRequestMethod("POST");
                    getProfileConnection.setDoInput(true);
                    getProfileConnection.setDoOutput(true);
                    String urlString = URLEncoder.encode("EmailAddress", "utf-8")+"="+URLEncoder.encode(emailAddress, "utf-8");
                    OutputStream toServerStream = getProfileConnection.getOutputStream();
                    BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream));
                    toServer.write(urlString);
                    toServer.flush();
                    toServer.close();
                    InputStream fromServerStream = getProfileConnection.getInputStream();
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "iso-8859-1"));
                    while ((line = fromServer.readLine()) != null) {
                        response = response + line;
                    }

                    getProfileConnection.disconnect();
                    return response;

                } catch (final Exception error) {
                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayMessage("Error Fetching Profile", error.getMessage());
                        }
                    });

                }
                break;
            case EXECUTE_UPDATE_ATHLETE_PROFILE:
                try {
                    String response = "";
                    String line = "";

                    String updateUrl = baseUrl + "update_athlete_profile.php";
                    URL updateURL = new URL(updateUrl);

                    String name = params[0];
                    String surname = params[1];
                    String emailAddress = params[2];
                    String password = params[3];
                    String weight = params[4];
                    String height = params[5];
                    String licenseNo = params[6];
                    String gender = params[7];

                    String urlData = URLEncoder.encode("Name", "utf-8")+"="+URLEncoder.encode(name, "utf-8")+"&"+URLEncoder.encode("Surname", "utf-8")+"="+URLEncoder.encode(surname, "utf-8")+"&"+URLEncoder.encode("EmailAddress", "utf-8")+"="+URLEncoder.encode(emailAddress, "utf-8")+"&"+URLEncoder.encode("Password", "utf-8")+"="+URLEncoder.encode(password, "utf-8")+"&"+URLEncoder.encode("Weight", "utf-8")+"="+URLEncoder.encode(weight, "utf-8")+"&"+URLEncoder.encode("Height", "utf-8")+"="+URLEncoder.encode(height, "utf-8")+"&"+URLEncoder.encode("LicenseNo", "utf-8")+"="+URLEncoder.encode(licenseNo, "utf-8")+"&"+URLEncoder.encode("Gender", "utf-8")+"="+URLEncoder.encode(gender, "utf-8");

                    HttpURLConnection updateConnection = (HttpURLConnection) updateURL.openConnection();
                    updateConnection.setRequestMethod("POST");
                    updateConnection.setDoInput(true);
                    updateConnection.setDoOutput(true);
                    OutputStream toServerStream = updateConnection.getOutputStream();
                    BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "utf-8"));
                    toServer.write(urlData);
                    toServer.flush();
                    toServer.close();

                    InputStream fromServerStream = updateConnection.getInputStream();
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "iso-8859-1"));
                    while ((line = fromServer.readLine()) != null) {
                        response = response +line;
                    }
                    fromServer.close();
                    updateConnection.disconnect();
                    return response;
                } catch (final Exception error) {
                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayMessage("Error Processing Profile Update", error.getMessage());
                        }
                    });

                }
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            switch (mode) {
                case EXECUTE_GET_ATHLETE_PROFILE:
                    if (!s.equals(null)) {

                        if (s.equalsIgnoreCase("nullError300")) {
                            displayMessage("No Profile Details Found", "No matching profile details were found");
                        } else if (s.equalsIgnoreCase("nullError200")) {
                            displayMessage("Error Getting Profile Detials", "An error occurred with sending the profile details to server");
                        } else {
                            JSONObject profileObject = new JSONObject(s);
                            nameEditText.setText(profileObject.getString("Name"));
                            surnameEditText.setText(profileObject.getString("Surname"));
                            emailEditText.setText(profileObject.getString("EmailAddress"));
                            passwordEditText.setText(profileObject.getString("Password"));
                            if (!profileObject.getString("Weight").equals("null")) {
                                weightEditText.setText(profileObject.getString("Weight"));
                            }
                            if (!profileObject.getString("Height").equals("null")) {
                                heightEditText.setText(profileObject.getString("Height"));
                            }
                            if (profileObject.getString("Gender").equalsIgnoreCase("Male")) {
                                genderSpinner.setSelectedIndex(1);
                            } else {
                                genderSpinner.setSelectedIndex(0);
                            }
                        }
                    }
                    break;
                case EXECUTE_UPDATE_ATHLETE_PROFILE:
                    if(!s.equalsIgnoreCase(null)) {
                        switch(s) {
                            case "Error200":
                            displayMessage("Error Updating Profile", "Please ensure you have valid data to update");
                                break;
                            case "Error500":
                                displayMessage("Error Executing Update", "An error occurred with updating your profile. Please try again later");                                break;
                            case "Success":
                                displayMessage("Update Successful", "We have successfully updated your profile details!");
                                break;
                                default:
                                    displayMessage("Unknown Error", s);
                                    break;
                        }
                    }
                    break;
            }
        } catch (Exception error) {
            displayMessage("Error Finalizing Profile", error.getMessage());
        }
    }

    public void displayMessage(String title, String message) {
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
