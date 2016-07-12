package Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Okuhle on 6/16/2016.
 */
public class SearchFragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList;
    private final List<String> fragmentTitleList;

    public SearchFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentTitleList = new ArrayList<>();
    }

    public void addFragment(Fragment newFragment, String fragmentTitle) {
        fragmentList.add(newFragment);
        fragmentTitleList.add(fragmentTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
