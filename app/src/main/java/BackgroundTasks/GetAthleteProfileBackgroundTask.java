package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.TextView;

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

import allblacks.com.iBaleka.R;


public class GetAthleteProfileBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentActivity;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private SharedPreferences appSharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private TextView totalEventRuns, totalPersonalRuns;

    public GetAthleteProfileBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.currentActivity);
        editor = appSharedPreferences.edit();

    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Fetching Athlete Profile");
        progressDialog.setMessage("Please wait while we fetch your athlete profile");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String emailAddress = params[0];
        String line = "";
        String response = "";
        try {
            String profileUrl = baseUrl + "get_athlete_runs.php";
            String url = URLEncoder.encode("EmailAddress", "utf-8")+"="+URLEncoder.encode(emailAddress, "utf-8");
            URL profileLink = new URL(profileUrl);
            HttpURLConnection profileConnection = (HttpURLConnection) profileLink.openConnection();
            profileConnection.setRequestMethod("POST");
            profileConnection.setDoInput(true);
            profileConnection.setDoOutput(true);
            OutputStream toServerStream = profileConnection.getOutputStream();
            BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "utf-8"));
            toServer.write(url);
            toServer.close();
            InputStream fromServerStream = profileConnection.getInputStream();
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "iso-8859-1"));
            while ((line = fromServer.readLine()) != null) {
                response = response +line;
            }
            fromServer.close();
            profileConnection.disconnect();
            return response;
        } catch (Exception error) {
            displayMessage("Error Processing Profile Request", error.getMessage());
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

        if (s.equalsIgnoreCase("Error300")) {
            displayMessage("Error Fetching Data", "The system could not retrieve your profile details due to no unique identifier being sent to the server");
        } else if (s.equals("Error200")) {
            displayMessage("No Matching Records", "The system was unable to find matching records");
        } else {
            JSONObject userObject = new JSONObject(s);
            editor.putString("TotalPersonalRuns", userObject.getString("TotalPersonalRuns"));
            editor.putString("TotalEventRuns", userObject.getString("TotalEventRuns"));
            editor.commit();
        }
    }
} catch (Exception error) {
    displayMessage("Error Finalizing Profile", error.getMessage());
}
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder box = new AlertDialog.Builder(currentActivity);
        box.setTitle(title);
        box.setMessage(message);
        box.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        box.show();
    }
}
