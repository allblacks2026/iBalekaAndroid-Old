package Fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Adapters.ProfileViewPageAdapter;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //Source: https://www.youtube.com/watch?v=8FiSyswS6RI
    private TabLayout profileTabLayout;
    private ViewPager profileViewPager;
    private ProfileViewPageAdapter profileViewPageAdapter;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.MainActivityToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        initializeComponents(myView);
        return myView;
    }

    private void initializeComponents(View myView) {
        profileTabLayout = (TabLayout) myView.findViewById(R.id.ProfileTabLayout);
        profileViewPager = (ViewPager) myView.findViewById(R.id.ProfilePager);
        profileViewPageAdapter = new ProfileViewPageAdapter(getChildFragmentManager());
        profileViewPageAdapter.addFragmentWithTitle(new UserProfileTabFragment(), "My Profile");
        profileViewPageAdapter.addFragmentWithTitle(new EditProfileFragment(), "Edit Profile");
        profileViewPageAdapter.addFragmentWithTitle(new UserActivityTabFragment(), "My Activity");
        profileViewPager.setAdapter(profileViewPageAdapter);
        profileTabLayout.setupWithViewPager(profileViewPager);
    }
    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRetainInstance();
    }
}
