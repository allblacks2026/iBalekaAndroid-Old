package allblacks.com.iBaleka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import BackgroundTasks.LoginBackgroundTask;
import Utilities.DeviceHardwareChecker;

public class AppSplashScreen extends AppCompatActivity {

    private DeviceHardwareChecker hardwareChecker;
    private boolean connectionResult = false;
    private SharedPreferences globalPreferences;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_splash_screen);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = globalPreferences.edit();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startLoginActivity();
            }
        };
        hardwareChecker = new DeviceHardwareChecker(this.getApplicationContext());
        hardwareChecker.checkNetworkConnection();
        connectionResult = hardwareChecker.isConnectedToInternet();
        if (connectionResult) {
            checkUserLogin();
        } else {
            displayMessage("No Internet Detected", "We could not detect an internet connection on" +
                    " your smartphone. Please check your network settings", listener);

        }
    }


    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        messageBox.show();
    }

    public void displayMessage(String title, String message, DialogInterface.OnClickListener
            listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", listener);
        messageBox.show();
    }

    public void startLoginActivity()
    {
        Intent newIntent = new Intent(this, LoginActivity.class);
        startActivity(newIntent);
        finish();
    }

    private void checkUserLogin()
    {
        if (globalPreferences.getBoolean("remember_credentials", false)) {

            String username = globalPreferences.getString("Username", "");
            String password = globalPreferences.getString("Password", "");
            if (username != "" && password != "") {
                //Check that the currently saved username and password match those on the system
                LoginBackgroundTask task = new LoginBackgroundTask(this);
                task.execute(username, password);
            } else {
                startLoginActivity();
            }
        } else {
            startLoginActivity();
        }
    }
}
