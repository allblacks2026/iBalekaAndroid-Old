package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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

import Models.Event;
<<<<<<< HEAD
=======
import allblacks.com.iBaleka.R;
>>>>>>> 6563102e0688568dacf9c9cc64df6123baa27909

/**
 * Created by Okuhle on 7/12/2016.
 */
public class SearchEventsBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentActivity;
    private SharedPreferences appSharedPreferences;
    private ProgressDialog progressDialog;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private List<Event> eventList;

    public SearchEventsBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        eventList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Processing Search");
        progressDialog.setMessage("Please wait while we process your search request");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String execScript = "search_events.php";
        String line = "";
        String response = "";

        String searchParam = params[0];
        String sortByDate = params[1];
        try {

            URL forgotPasswordLink = new URL(execScript);
            HttpURLConnection forgetPasswordConnection = (HttpURLConnection)
                    forgotPasswordLink.openConnection();
            forgetPasswordConnection.setRequestMethod("POST");
            forgetPasswordConnection.setDoOutput(true);
            forgetPasswordConnection.setDoInput(true);

            String forgotPasswordString = URLEncoder.encode("SearchCriteria", "utf-8")
                    +"="+URLEncoder.encode(searchParam, "utf-8")+"&"+URLEncoder.encode("SortByDate", "utf-8")+"="+URLEncoder.encode(sortByDate, "utf-8");
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

        } catch (final Exception error) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayMessage("Error Processing Search", error.getMessage());
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        try {
            if (!s.equals(null)) {
                if (s.equals("Error300")) {
                    displayMessage("No Results Found", "Your search yielded no results. Please try again");
                } else if (s.equals("Error400")) {
                    displayMessage("Missing Data", "Please ensure you have specified a valid search criteria");
                } else {
                    JSONArray array = new JSONArray(s);
                    for (int a = 0; a < array.length(); a++) {
                        JSONObject currentResult = array.getJSONObject(0);
                        Event newEvent = new Event(currentResult.getString("EventID"), currentResult.getString("Description"), currentResult.getString("Date"), currentResult.getString("Time"), currentResult.getString("Location"));
                        eventList.add(newEvent);
                    }
                }
            }else {
                displayMessage("No Results Found", "No results were found. Try searching again with different parameters");
            }
        } catch (final Exception error) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayMessage("Error Parsing Data", error.getMessage());
                }
            });
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
