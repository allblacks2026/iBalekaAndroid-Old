package Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;


//References: http://developer.android
// .com/training/monitoring-device-state/connectivity-monitoring.html
public class DeviceHardwareChecker {

    private Context currentContext;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    private boolean isGPSEnabled;
    private PackageManager packageManager;

    public DeviceHardwareChecker(Context currentContext) {
        this.currentContext = currentContext;
        connectivityManager = (ConnectivityManager) currentContext.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        packageManager = currentContext.getPackageManager();
    }

    public void checkNetworkConnection()
    {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnected();
    }

    public boolean isConnectedToInternet()
    {
        return isConnected;
    }

    public void checkGPSReceiver()
    {
        boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION);
        if (hasGPS) {
            LocationManager manager = (LocationManager) currentContext.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                isGPSEnabled = false;
            } else {
                isGPSEnabled = true;
            }
        } else {
            isGPSEnabled = false;
        }
    }

    public boolean getGPSStatus() {
        return isGPSEnabled;
    }




}
