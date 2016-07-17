package Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import Adapters.SearchResultsAdapter;
import BackgroundTasks.SearchEventsBackgroundTask;
import Models.Event;
import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {
    private RecyclerView searchResultsRecyclerView;
    private SearchEventsBackgroundTask searchEventsBackgroundTask;
    private SharedPreferences appSharedPreferences;
    private SearchResultsAdapter searchAdapter;
    private List<Event> eventsList;
    private TabLayout searchTabLayout;

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



    }

    private void startSearch()
    {
            String searchCriteria = appSharedPreferences.getString("SearchCriteria", "");
            boolean sortByDate = appSharedPreferences.getBoolean("SortByDate", false);
            searchEventsBackgroundTask = new SearchEventsBackgroundTask(getActivity());
            searchAdapter = new SearchResultsAdapter(getActivity());
            searchEventsBackgroundTask.execute(searchCriteria);
            eventsList = searchEventsBackgroundTask.getEventsList();
            if (eventsList.size() != 0) {
                searchAdapter.setEventsList(eventsList);
                searchResultsRecyclerView.setAdapter(searchAdapter);
            }
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder dialogMessage = new AlertDialog.Builder(getActivity());
        dialogMessage.setTitle(title);
        dialogMessage.setMessage(message);
        dialogMessage.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogMessage.show();
    }

}
