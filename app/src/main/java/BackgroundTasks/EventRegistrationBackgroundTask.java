package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Okuhle on 7/25/2016.
 */
public class EventRegistrationBackgroundTask extends AsyncTask<String, String, String> {

    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private Activity currentActivity;
    private ProgressDialog progressDialog;

    public EventRegistrationBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Registering For Event");
        progressDialog.setMessage("Please wait while we try register you for the event");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String line = "";
            String response = "";
            String emailAddress = params[1];
            String eventID = params[0];
            String urlLink = baseUrl + "register_athlete_for_event.php";
            String parameters = URLEncoder.encode("EmailAddress", "utf-8")+"="+URLEncoder.encode(emailAddress, "utf-8")+"&"+URLEncoder.encode("EventID", "utf-8")+"="+URLEncoder.encode(eventID, "utf-8");

            URL link = new URL(urlLink);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream toServerStream = connection.getOutputStream();
            BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "utf-8"));
            toServer.write(parameters);
            toServer.flush();
            toServer.close();
            InputStream fromServerStream = connection.getInputStream();
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "iso-8859-1"));
            while ((line = fromServer.readLine()) != null) {
                response = response + line;
            }
            fromServer.close();
            connection.disconnect();
            return response;
        } catch (final Exception error) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayMessage("Error Processing Registration", error.getMessage());
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

        if (!s.equals(null)){
            if (s.equalsIgnoreCase("Success")) {
                displayMessage("Successful Registration", "You have successfully registered for the selected event!");
            } else if (s.equalsIgnoreCase("Error100")) {
                displayMessage("Error Finalizing Registration", "The server did not receive any user information.");
            } else if (s.equalsIgnoreCase("Error200")) {
                displayMessage("Athlete Not Found", "The athlete you tried registering no longer exists on our records");
            } else if (s.equalsIgnoreCase("Error300")) {
                displayMessage("Athlete Already Registered", "You are already registered for this event. Please register for another event");
            } else if (s.equalsIgnoreCase("Error400")){
                displayMessage("Error Registering Athlete", "An error occurred while registering the athlete for the event");
            }
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
