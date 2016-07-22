package Listeners;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import Fragments.AthleteLandingFragment;
import Fragments.ProfileFragment;
import Fragments.SearchFragment;
import Fragments.StartRunFragment;
import allblacks.com.Activities.R;

/**
 * Created by Okuhle on 3/28/2016.
 */
public class NavigationMenuOnItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private Activity currentActivity;
    private FragmentManager mgr;
    private DrawerLayout drawerLayout;
    private TextView toolbarTextView;
    private NavigationView navigationView;

    public NavigationMenuOnItemSelectedListener(AppCompatActivity currentActivity)
    {
        this.currentActivity = currentActivity;
        mgr = currentActivity.getFragmentManager();
        drawerLayout = (DrawerLayout) currentActivity.findViewById(R.id.menuDrawerLayout);
        toolbarTextView = (TextView) currentActivity.findViewById(R.id.MainActivityTextView);
        navigationView = (NavigationView) currentActivity.findViewById(R.id.mainActivityNavigationView);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.athleteHomeOption:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                AthleteLandingFragment homeFragment = new AthleteLandingFragment();
                FragmentTransaction trans = mgr.beginTransaction();
                trans.replace(R.id.MainActivityContentArea, homeFragment, "HomeFragment");
                trans.addToBackStack("HomeFragment");
                trans.commit();
                toolbarTextView.setText("Welcome, User"); //code to get the user falls here
                drawerLayout.closeDrawers();
                break;
            case R.id.athleteViewProfile:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction thirdTrans = mgr.beginTransaction();
                thirdTrans.replace(R.id.MainActivityContentArea, profileFragment,
                        "ProfileFragment");
                thirdTrans.addToBackStack("ProfileFragment");
                toolbarTextView.setText("Athlete Profile Details");
                drawerLayout.closeDrawers();
                thirdTrans.commit();
                break;
            case R.id.athleteStartRun:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                StartRunFragment startRunFragment = new StartRunFragment();
                FragmentTransaction startRunTransaction = mgr.beginTransaction();
                startRunTransaction.replace(R.id.MainActivityContentArea, startRunFragment,
                        "StartRunFragment");
                startRunTransaction.addToBackStack("StartRunFragment");
                toolbarTextView.setText("Start a Run");
                drawerLayout.closeDrawers();
                startRunTransaction.commit();
                break;
            case R.id.searchEvents:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                SearchFragment searchFragment = new SearchFragment();
                FragmentTransaction searchFragmentTransaction = mgr.beginTransaction();
                searchFragmentTransaction.replace(R.id.MainActivityContentArea, searchFragment,
                        "SearchFragment");
                searchFragmentTransaction.addToBackStack("SearchFragment");
                toolbarTextView.setText("Search Events");
                drawerLayout.closeDrawers();
                searchFragmentTransaction.commit();
                break;
        }
        return true;
    }
}
