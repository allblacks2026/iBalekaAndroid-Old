package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

<<<<<<< HEAD
import Adapters.SearchFragmentAdapter;
import allblacks.com.Activities.R;
=======
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import allblacks.com.iBaleka.R;
>>>>>>> 6563102e0688568dacf9c9cc64df6123baa27909

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private ViewPager searchViewPager;
    private TabLayout searchTabLayout;
    private Toolbar mainActivityToolbar;
    private SearchFragmentAdapter searchAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_search, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        searchTabLayout = (TabLayout) currentView.findViewById(R.id.SearchTabLayout);
        searchViewPager = (ViewPager) currentView.findViewById(R.id.searchViewPager);
        searchAdapter = new SearchFragmentAdapter(getChildFragmentManager());
        StartSearchFragment searchFragment = new StartSearchFragment();
        SearchResultsFragment resultsFragment = new SearchResultsFragment();
        searchAdapter.addFragment(searchFragment, "Search");
        searchAdapter.addFragment(resultsFragment, "Results");
        searchViewPager.setAdapter(searchAdapter);
        searchTabLayout.setupWithViewPager(searchViewPager);

    }

}
