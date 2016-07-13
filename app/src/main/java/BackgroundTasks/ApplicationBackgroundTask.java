package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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

public class ApplicationBackgroundTask extends AsyncTask<String, String, String> {

    private Activity currentActivity;
    private ProgressDialog progressDialog;
    private ExecutionMode executionMode;
    private String baseUrl = "http://sict-iis.nmmu.ac.za/AllBlacks/";

    public ApplicationBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void setExecutionMode(ExecutionMode mode)
    {
        executionMode = mode;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        if (executionMode == ExecutionMode.EXECUTE_GET_ATHLETE_PROFILE) {
            progressDialog.setTitle("Fetching Profile Details");
            progressDialog.setMessage("Please wait while we fetch your profile Information");
            progressDialog.show();
        } else if (executionMode == ExecutionMode.EXECUTE_SEARCH) {
            progressDialog.setTitle("Searching The System");
            progressDialog.setMessage("Please wait while we search our system");
            progressDialog.show();
        } else if (executionMode == ExecutionMode.EXECUTE_GET_ATHLETE_ACTIVITY) {
            progressDialog.setTitle("Fetching Your Activity Data");
            progressDialog.setMessage("Please wait while we fetch your activity information");
            progressDialog.show();
        } else if (executionMode == ExecutionMode.GET_ATHLETE_SUMMARY) {
            progressDialog.setTitle("Fetching Athlete Summary");
            progressDialog.setMessage("Please wait while we fetch your summary details");
            progressDialog.show();
        }
    }
    @Override
    protected String doInBackground(String... params) {


        switch (executionMode) {
            case EXECUTE_GET_ATHLETE_ACTIVITY:
                try {
                    String link = baseUrl + "/Athlete/Get/";
                    String line = "";
                    String response = "";
                    //Assuming we are passing the athlete's profile ID
                    String profileParams =  URLEncoder.encode("Athlete", "UTF-8")+"="+URLEncoder.encode(params[0] + "UTF-8");
                    URL profileUrl = new URL(link);
                    HttpURLConnection profileConnection = (HttpURLConnection) profileUrl.openConnection();
                    profileConnection.setDoOutput(true);
                    profileConnection.setDoInput(true);
                    profileConnection.setRequestMethod("GET"); //Get Profile
                    OutputStream toServerStream = profileConnection.getOutputStream();
                    BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "UTF-8"));
                    toServer.write(profileParams);
                    toServer.flush();
                    toServer.close();
                    InputStream fromServerStream = profileConnection.getInputStream();
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "ISO-8859-1"));
                    while ((line = fromServer.readLine()) != null) {
                        response = response + line;
                    }
                    fromServer.close();
                    fromServerStream.close();
                    profileConnection.disconnect();
                    return response;
                } catch (Exception error) {
                    displayMessage("Error Getting Activity Information", error.getMessage());

                }
            break;

            case EXECUTE_SEARCH:
                try {
                    String searchUrl = baseUrl + "/Events/Search/";
                    String searchText = params[0].trim();
                    String searchLocation = params[1].trim();
                    String searchCity = params[2].trim();
                    String orderByCity = params[3].trim();
                    String line = "";
                    String response = "";

                    String searchData = URLEncoder.encode("SearchTerm", "UTF-8")+"="+URLEncoder.encode(searchText, "UTF-8")+"="+URLEncoder.encode("SearchLocation", "UTF-8")+"="+URLEncoder.encode(searchLocation, "UTF-8")+URLEncoder.encode("SearchCity", "UTF-8")+"="+URLEncoder.encode(searchCity, "UTF-8")+URLEncoder.encode("SortResults" + "UTF-8")+"="+URLEncoder.encode(orderByCity, "UTF-8");
                    URL searchURL = new URL(searchUrl);
                    HttpURLConnection searchConnection = (HttpURLConnection) searchURL.openConnection();
                    searchConnection.setDoInput(true);
                    searchConnection.setDoOutput(true);
                    searchConnection.setRequestMethod("GET");
                    OutputStream toServerStream = searchConnection.getOutputStream();
                    BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "UTF-8"));
                    toServer.write(searchData);
                    toServer.flush();
                    toServer.close();
                    InputStream fromServerStream = searchConnection.getInputStream();
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "ISO-8859-1"));
                    while ((line = fromServer.readLine()) != null) {
                        response = response + line;
                    }
                    fromServer.close();
                    fromServerStream.close();
                    searchConnection.disconnect();
                    return response;
                } catch (Exception error) {
                    displayMessage("Error Searching For Events", error.getMessage());

                }
                break;
            case EXECUTE_GET_ATHLETE_PROFILE:
                try {
                    String profileUrl = baseUrl + "/Athlete/Get/";
                    String urlParams = URLEncoder.encode("AthleteID", "UTF-8") + "=" + URLEncoder.encode(profileUrl, "UTF-8");
                    String line = "";
                    String response = "";
                    URL profileLink = new URL(profileUrl);
                    HttpURLConnection profileConnection = (HttpURLConnection) profileLink.openConnection();
                    profileConnection.setDoInput(true);
                    profileConnection.setDoOutput(true);
                    profileConnection.setRequestMethod("GET");
                    OutputStream toServerStream = profileConnection.getOutputStream();
                    BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "UTF-8"));
                    toServer.write(urlParams);
                    toServer.flush();
                    toServer.close();
                    InputStream fromServerStream = profileConnection.getInputStream();
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "ISO-8859-1"));
                    while ((line = fromServer.readLine()) != null) {
                        response = response + line;
                    }
                    profileConnection.disconnect();
                    return response;
                } catch (Exception error) {
                    displayMessage("Error Getting Athlete Profile", error.getMessage());

                }break;

            case GET_ATHLETE_SUMMARY:
                try {
                    String line = "";
                    String response = "";
                    String athleteLink = baseUrl + "/Athlete/Summary";
                    String athleteID = params[0].trim();
                    String request = URLEncoder.encode("AthleteID", "UTF-8")+"="+URLEncoder.encode(athleteID, "UTF-8");
                    URL summaryUrl = new URL(athleteLink);
                    HttpURLConnection athleteConnection = (HttpURLConnection) summaryUrl.openConnection();
                    athleteConnection.setDoInput(true);
                    athleteConnection.setDoOutput(true);
                    athleteConnection.setRequestMethod("GET");
                    OutputStream toServerStream = athleteConnection.getOutputStream();
                    BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(toServerStream, "UTF-8"));
                    toServer.write(request);
                    toServer.flush();
                    toServer.close();
                    InputStream fromServerStream = athleteConnection.getInputStream();
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(fromServerStream, "ISO-8859-1"));
                    while ((line = fromServer.readLine()) != null) {
                        response = response + line;
                    }
                    return response;
                } catch (Exception errror) {
                    displayMessage("Error Fetching Summary Details", "An error occurred when collecting your latest summary information");

                }
                break;
            
            default:

                break;
        }
        return null;
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder dialogMessage = new AlertDialog.Builder(currentActivity);
        dialogMessage.setTitle(title);
        dialogMessage.setMessage(message);
        dialogMessage.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogMessage.show();
    }

    @Override
    protected void onPostExecute(String s) {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }

        switch (executionMode) {
            case EXECUTE_GET_ATHLETE_PROFILE:
                try {
                    JSONArray responseArray = new JSONArray(s);
                    for (int a = 0; a < responseArray.length(); a++) {


                    }
                } catch (Exception error) {
                    if (s.isEmpty()){
                        displayMessage("No Profile Information", "No profile information could be found");
                    }
                }

                break;
            case EXECUTE_SEARCH:
                try {

                } catch (Exception error) {
                    if (s.isEmpty()) {
                        displayMessage("No Search Information", "No search results were found");
                    }
                }
        }
    }
}
