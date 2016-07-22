package Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import BackgroundTasks.SearchEventsBackgroundTask;
import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {

    private RecyclerView searchResultsRecyclerView;
    private SearchEventsBackgroundTask searchEventsBackgroundTask;
    private SharedPreferences appSharedPreferences;

    public SearchResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_search_results, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View thisView) {
        searchResultsRecyclerView = (RecyclerView) thisView.findViewById(R.id
                .EventSearchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appSharedPreferences = getActivity().getSharedPreferences("iBaleka_Preferences", Context.MODE_PRIVATE);


    }

}
