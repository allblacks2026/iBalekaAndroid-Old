package allblacks.com.iBaleka;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import Fragments.AboutApplicationFragment;
import Fragments.ApplicationPreferencesFragment;
import Fragments.AthleteLandingFragment;
import Fragments.EditProfileFragment;
import Listeners.NavigationMenuOnItemSelectedListener;
import Utilities.iBalekaSingleton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mainActivityToolbar;
    private TextView toolbarTextView;
    private ImageView toolbarImage;
    private FragmentManager mgr;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private SharedPreferences activityPreferences;
    private SharedPreferences appSharedPreferences;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initializeControls();
            loadLandingScreenFragment();
            iBalekaSingleton.setContext(this.getApplicationContext());
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initializeControls();
        }
    }

    public void initializeControls()
    {
        mainActivityToolbar = (Toolbar) findViewById(R.id.MainActivityToolbar);
        toolbarTextView = (TextView) findViewById(R.id.MainActivityTextView);
        toolbarImage = (ImageView) findViewById(R.id.MainActivityImageView);
        drawerLayout = (DrawerLayout) findViewById(R.id.menuDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.mainActivityNavigationView);
        setSupportActionBar(mainActivityToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //Code to determine the type of login - switch menu according to the user
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainActivityToolbar, R.string
                .app_name, R
                .string
                .app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView.inflateHeaderView(R.layout.navigation_menu_header);
        navigationView.inflateMenu(R.menu.athlete_navigation_menu);
        toolbarImage.setOnClickListener(this);
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        activityPreferences = getSharedPreferences("iBaleka", MODE_PRIVATE);
        editor = activityPreferences.edit();
        NavigationMenuOnItemSelectedListener listener = new NavigationMenuOnItemSelectedListener
                (this);
        navigationView.setNavigationItemSelectedListener(listener);
        toolbarImage.setImageResource(R.drawable.ibaleka_logo);
        mgr = getFragmentManager();
        mapComponents();

    }
    public void loadLandingScreenFragment()
    {
        AthleteLandingFragment landingFragment = new AthleteLandingFragment();
        FragmentTransaction transaction = mgr.beginTransaction();
        transaction.replace(R.id.MainActivityContentArea, landingFragment, "UserStats");
        transaction.addToBackStack("UserStats");
        transaction.commit();
    }

    private void mapComponents()
    {
        View headerView = navigationView.getHeaderView(0);
        TextView athleteNameSurname = (TextView) headerView.findViewById(R.id.profileNameSurname);
        TextView emailAddress = (TextView) headerView.findViewById(R.id.profileEmailAddress);

        String nameSurname = appSharedPreferences.getString("Name", "") + " "+ appSharedPreferences.getString("Surname", "");
        athleteNameSurname.setText(nameSurname);
        emailAddress.setText(appSharedPreferences.getString("EmailAddress", "").toLowerCase());

    }
    @Override
    protected void onPause() {
        editor.putString("ToolbarText", toolbarTextView.getText().toString());
        editor.commit();
        super.onPause();
    }
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText(activityPreferences.getString("ToolbarText", ""));
    }
    @Override
    public void onBackPressed() {
        if (mgr.getBackStackEntryCount() > 0) {
            mgr.popBackStack();
        } else {
            super.onBackPressed();
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onClick(View v) {
        drawerLayout.closeDrawers();
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.athlete_navigation_menu);
        AthleteLandingFragment thisOne = new AthleteLandingFragment();
        FragmentTransaction transaction = mgr.beginTransaction();
        transaction.replace(R.id.MainActivityContentArea, thisOne, "HomeFragment");
        transaction.addToBackStack("HomeFragment");
        toolbarTextView.setText("Welcome");
        transaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.in_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           case (R.id.inAppApplicationPreferences):
               ApplicationPreferencesFragment preferencesFragment = new
                       ApplicationPreferencesFragment();
            FragmentTransaction preferencesTransaction = mgr.beginTransaction();
               preferencesTransaction.replace(R.id.MainActivityContentArea, preferencesFragment,
                       "PreferencesFragment");
               toolbarTextView.setText("Application Settings");
               preferencesTransaction.addToBackStack(null);
               preferencesTransaction.commit();
            break;
            case (R.id.inAppEditProfile) :
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                FragmentTransaction profileTrans = mgr.beginTransaction();
                profileTrans.replace(R.id.MainActivityContentArea, editProfileFragment, "EditProfileFragment");
                toolbarTextView.setText("Edit Profile");
                profileTrans.addToBackStack(null);
                profileTrans.commit();
                break;
            case (R.id.inAppAboutApplication):
                AboutApplicationFragment aboutAppFragment = new AboutApplicationFragment();
                FragmentTransaction trans = mgr.beginTransaction();
                trans.replace(R.id.MainActivityContentArea, aboutAppFragment, "AboutApplicationFragment");
                trans.addToBackStack(null);
                toolbarTextView.setText("About Application");
                trans.commit();
                break;
        }
        return true;
    }
}
