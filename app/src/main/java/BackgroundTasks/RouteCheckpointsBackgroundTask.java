package BackgroundTasks;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.List;
import Models.Checkpoint;
import Utilities.DataContainer;

public class RouteCheckpointsBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentActivity;
    private ProgressDialog progressDialog;
    private List<Checkpoint> checkpointsList;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private SharedPreferences appPreferences;
    private SharedPreferences.Editor editor;

    public RouteCheckpointsBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        checkpointsList = new ArrayList<>();
        appPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        editor = appPreferences.edit();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Getting Map Data");
        progressDialog.setMessage("Please wait while locate the map data");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String line = "";
            String response = "";
            String mapUrl = baseUrl + "get_route_checkpoints.php";
            String parameters = URLEncoder.encode("EventID", "utf-8")+"="+URLEncoder.encode(params[0], "utf-8");
            URL url = new URL(mapUrl);
            HttpURLConnection checkpointConnection = (HttpURLConnection) url.openConnection();
            checkpointConnection.setRequestMethod("POST");
            checkpointConnection.setDoInput(true);
            checkpointConnection.setDoOutput(true);
            OutputStream toServerStream = checkpointConnection.getOutputStream();
            BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "utf-8"));
            toServer.write(parameters);
            toServer.flush();
            toServer.close();

            InputStream fromServerStream = checkpointConnection.getInputStream();
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "iso-8859-1"));
            while ((line = fromServer.readLine()) != null) {
                response = response + line;
            }
            fromServer.close();
            checkpointConnection.disconnect();
            return response;
        } catch (final Exception error) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayMessage("Error Getting Map Data", error.getMessage());
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
                if (s.equalsIgnoreCase("Error100")) {
                    displayMessage("No Event Sent", "An error occurred while getting event details sent to the server");
                } else if (s.equals("Error200")) {
                    displayMessage("No Map Data Found", "The Event you tried getting map data for has no saved route information");
                } else {
                    JSONArray array = new JSONArray(s);
                    for (int a = 0; a < array.length(); a++) {
                        JSONObject currentPoint = array.getJSONObject(a);
                        Checkpoint newPoint = new Checkpoint(currentPoint.getString("CheckpointID"), currentPoint.getString("Latitude"), currentPoint.getString("Longitude"));
                        checkpointsList.add(newPoint);
                    }
                    DataContainer.getDataContainerInstance().setCheckpointList(checkpointsList);
                }
            }
        } catch (final Exception error) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayMessage("Error Finalizing Map Data", error.getMessage());
                }
            });
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
