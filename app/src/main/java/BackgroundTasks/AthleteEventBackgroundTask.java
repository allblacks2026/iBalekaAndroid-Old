package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

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

import Adapters.RegisteredEventsAdapter;
import Models.RegisteredEvent;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 7/25/2016.
 */
public class AthleteEventBackgroundTask extends AsyncTask<String, String, String> {

    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private Activity currentActivity;
    private ProgressDialog progressDialog;
    private List<RegisteredEvent> registeredEventList;

    public AthleteEventBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        registeredEventList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Getting Registered Events");
        progressDialog.setMessage("Please wait while we get your registered events");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            String line = "";
            String response = "";
            String emailAddress = params[0];
            String urlEncode = URLEncoder.encode("EmailAddress", "utf-8")+"="+URLEncoder.encode(emailAddress, "utf-8");
            String link = baseUrl + "get_athlete_registered_events.php";
            URL url = new URL(link);
            HttpURLConnection regConnection = (HttpURLConnection) url.openConnection();
            regConnection.setRequestMethod("POST");
            regConnection.setDoInput(true);
            regConnection.setDoOutput(true);
            OutputStream toServerStream = regConnection.getOutputStream();
            BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "utf-8"));
            toServer.write(urlEncode);
            toServer.flush();
            toServer.close();
            InputStream fromServerStream = regConnection.getInputStream();
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "iso-8859-1"));
            while ((line = fromServer.readLine()) != null) {
                response = response + line;
            }
            fromServer.close();
            regConnection.disconnect();
            return response;
        } catch (final Exception error) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayMessage("Error Processing Task", error.getMessage());
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
            if (s.equals("Error100")) {
                displayMessage("No Athlete Data Received", "The server did not receive any athlete data");
            } else if (s.equals("Error200")) {
                displayMessage("No Registered Events", "No event registrations were found. Please register for an event");
            } else {
                JSONArray array = new JSONArray(s);
                if (array != null) {
                    for (int a = 0; a < array.length(); a++) {
                        JSONObject object = array.getJSONObject(a);
                        RegisteredEvent thisEvent = new RegisteredEvent(object.getString("AthleteID"), object.getString("EventID"), object.getString("Description"), object.getString("Date"), object.getString("Time"), object.getString("Location"));
                        registeredEventList.add(thisEvent);
                    }

                    RecyclerView registeredEventsRecyclerView = (RecyclerView) currentActivity.findViewById(R.id.RegisteredEventsRecyclerView);
                    RegisteredEventsAdapter adapter = new RegisteredEventsAdapter(currentActivity);
                    adapter.setRegisteredEventList(registeredEventList);
                    registeredEventsRecyclerView.setAdapter(adapter);
                } else {
                    displayMessage("Error", s);
                }
            }
        }

        } catch (Exception error) {
            displayMessage("Error Getting Registered Events",error.getMessage());
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
