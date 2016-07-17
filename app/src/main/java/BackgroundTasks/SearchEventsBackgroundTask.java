package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import Adapters.SearchResultsAdapter;
import Models.Event;
import allblacks.com.Activities.R;

/**
 * Created by Okuhle on 7/12/2016.
 */
public class SearchEventsBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentActivity;
    private SharedPreferences appSharedPreferences;
    private ProgressDialog progressDialog;
    private String baseUrl = "http://154.127.61.157/ibaleka/";
    private List<Event> eventList;
    private RecyclerView searchRecyclerView;
    private SearchResultsAdapter searchAdapter;
    private String searchLocation, telephoneNumber, website;

    public SearchEventsBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        eventList = new ArrayList<>();
        searchRecyclerView = (RecyclerView) currentActivity.findViewById(R.id.EventSearchResultsRecyclerView);
        searchAdapter = new SearchResultsAdapter(currentActivity);
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

        String execScript = baseUrl + "search_events.php";
        String line = "";
        String response = "";

        searchLocation = params[0];
        telephoneNumber = params[1];
        try {

            URL forgotPasswordLink = new URL(execScript);
            HttpURLConnection forgetPasswordConnection = (HttpURLConnection)
                    forgotPasswordLink.openConnection();
            forgetPasswordConnection.setRequestMethod("POST");
            forgetPasswordConnection.setDoOutput(true);
            forgetPasswordConnection.setDoInput(true);

            String forgotPasswordString = URLEncoder.encode("SearchCriteria", "utf-8")
                    +"="+URLEncoder.encode(searchLocation, "utf-8")+"&"+URLEncoder.encode("SortByDate", "utf-8")+"="+URLEncoder.encode(telephoneNumber, "utf-8");
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
                        JSONObject currentResult = array.getJSONObject(a);
                        Event newEvent = new Event(currentResult.getString("EventID"), currentResult.getString("Description"), currentResult.getString("Date"), currentResult.getString("Time"), currentResult.getString("Location"));
                        eventList.add(newEvent);
                    }

                    if (eventList.size() != 0) {
                        searchAdapter.setEventsList(eventList);
                        searchRecyclerView.setAdapter(searchAdapter);
                    }
                }
            }else {
                displayMessage("No Results Found", "Your search criteria "+searchLocation +" did not return any results. Please try again.");
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

    public List<Event> getEventsList()
    {
        return eventList;
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
