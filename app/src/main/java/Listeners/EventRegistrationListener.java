package Listeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Created by Okuhle on 7/25/2016.
 */
public class EventRegistrationListener implements View.OnClickListener {

    private Activity currentActivity;
    private SharedPreferences appPreferences;

    public EventRegistrationListener(Activity currentActivity) {
        this.currentActivity = currentActivity;
        appPreferences = PreferenceManager.getDefaultSharedPreferences(this.currentActivity);
    }
    @Override
    public void onClick(View v) {

    }
}
