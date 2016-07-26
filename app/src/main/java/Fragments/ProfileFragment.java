package Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Adapters.ProfileViewPageAdapter;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ViewPager.OnPageChangeListener {

    //Source: https://www.youtube.com/watch?v=8FiSyswS6RI
    private TabLayout profileTabLayout;
    private ViewPager profileViewPager;
    private ProfileViewPageAdapter profileViewPageAdapter;
    private TextView toolbarTextView;
    private SharedPreferences profilePreferences;
    private SharedPreferences.Editor profileEditor;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePreferences = getActivity().getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE);
        profileEditor = profilePreferences.edit();
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
        profileViewPager.addOnPageChangeListener(this);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        profileViewPageAdapter = new ProfileViewPageAdapter(getChildFragmentManager());
        profileViewPageAdapter.addFragmentWithTitle(new UserProfileTabFragment(), "My Profile");
        profileViewPageAdapter.addFragmentWithTitle(new EditProfileFragment(), "Edit Profile");
        profileViewPageAdapter.addFragmentWithTitle(new UserActivityTabFragment(), "My Activity");
        profileViewPager.setAdapter(profileViewPageAdapter);
        profileTabLayout.setupWithViewPager(profileViewPager);
        toolbarTextView.setText("View Your Profile");
    }
    @Override
    public void onPause() {
        profileEditor.putString("ToolbarText", toolbarTextView.getText().toString());
        profileEditor.commit();
        setRetainInstance(true);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRetainInstance();
        toolbarTextView.setText(profilePreferences.getString("ToolbarText", ""));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            profileEditor.putString("ToolbarText", "View Your Profile");
            toolbarTextView.setText("View Your Profile");
            profileEditor.commit();
        } else if (position == 1) {
            profileEditor.putString("ToolbarText", "Edit Your Profile");
            toolbarTextView.setText("Edit Your Profile");
            profileEditor.commit();
        } else if (position == 2) {
            profileEditor.putString("ToolbarText", "View Your Activity");
            toolbarTextView.setText("View Your Activity");
            profileEditor.commit();
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            profileEditor.putString("ToolbarText", "View Your Profile");
            toolbarTextView.setText("View Your Profile");
            profileEditor.commit();
        } else if (position == 1) {
            profileEditor.putString("ToolbarText", "Edit Your Profile");
            toolbarTextView.setText("Edit Your Profile");
            profileEditor.commit();
        } else if (position == 2) {
            profileEditor.putString("ToolbarText", "View Your Activity");
            toolbarTextView.setText("View Your Activity");
            profileEditor.commit();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
