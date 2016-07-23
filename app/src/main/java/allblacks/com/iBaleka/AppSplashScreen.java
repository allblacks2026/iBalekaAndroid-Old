package allblacks.com.iBaleka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Utilities.DeviceHardwareChecker;

public class AppSplashScreen extends AppCompatActivity {

    private DeviceHardwareChecker hardwareChecker;
    private boolean connectionResult = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_splash_screen);

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
            startLoginActivity();
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
}
