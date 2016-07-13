package Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class ProfileViewPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList;
    private ArrayList<String> tabList;

    public ProfileViewPageAdapter(FragmentManager manager)
   {
       super(manager); //Something needs to be done here
       fragmentList = new ArrayList<>();
       tabList = new ArrayList<>();
   }

    public void addFragmentWithTitle(Fragment newFragment, String title)
    {
        fragmentList.add(newFragment);
        tabList.add(title);
    }

    public int getCount() {

        return fragmentList.size();
    }

    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }


}
