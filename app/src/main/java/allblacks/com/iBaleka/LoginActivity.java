package allblacks.com.iBaleka;

import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import Fragments.AboutApplicationFragment;
import Fragments.ApplicationPreferencesFragment;
import Fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    private Toolbar loginActivityToolbar;
    private ImageView loginActivityImageView;
    private TextView loginActivityTextView;
    private FragmentManager fragmentManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponents(savedInstanceState);
        }

    private void initializeComponents(Bundle savedInstanceState)
    {
        loginActivityToolbar = (Toolbar) findViewById(R.id.LoginActivityToolbar);
        loginActivityImageView = (ImageView) findViewById(R.id.LoginActivityImageView);
        loginActivityTextView = (TextView) findViewById(R.id.LoginActivityToolbarTextView);
        fragmentManager = getFragmentManager();
        setSupportActionBar(loginActivityToolbar);
        getSupportActionBar().setTitle(null);
        loginActivityImageView.setImageResource(R.drawable.ibaleka_logo);
        if (savedInstanceState == null) {
            loadLoginFragment();
        }
    }

    private void loadLoginFragment()
    {
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction loginTransaction = fragmentManager.beginTransaction();
        loginTransaction.replace(R.id.LoginActivityContentArea, loginFragment, "LoginFragment");
        loginTransaction.addToBackStack("LoginFragment");
        loginActivityTextView.setText("Please Login to Continue");
        loginTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.applicationPreferences:
                ApplicationPreferencesFragment preferencesFragment = new
                        ApplicationPreferencesFragment();
                FragmentTransaction preferencesTransaction = fragmentManager.beginTransaction();
                preferencesTransaction.replace(R.id.LoginActivityContentArea,
                        preferencesFragment, "PreferencesFragment");
                preferencesTransaction.addToBackStack("PreferencesFragment");
                preferencesTransaction.commit();
                break;
            case R.id.aboutApplication:
                AboutApplicationFragment aboutApplicationFragment = new AboutApplicationFragment();
                FragmentTransaction  aboutApplicationTransaction = fragmentManager
                        .beginTransaction();
                aboutApplicationTransaction.replace(R.id.LoginActivityContentArea,
                        aboutApplicationFragment, "AboutApplicationFragment");
                aboutApplicationTransaction.addToBackStack("AboutApplicationFragment");
                aboutApplicationTransaction.commit();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            loginActivityTextView.setText("Please Login to Continue");
            super.onBackPressed();
        }
    }
}
