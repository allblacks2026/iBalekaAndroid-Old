package Fragments;


<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
<<<<<<< HEAD
=======
=======
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
<<<<<<< HEAD
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
=======
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
=======
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import BackgroundTasks.SearchEventsBackgroundTask;
=======
=======
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
import java.util.ArrayList;
import java.util.List;

import Adapters.SearchResultsAdapter;
import BackgroundTasks.SearchEventsBackgroundTask;
import Models.Event;
<<<<<<< HEAD
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
=======
import BackgroundTasks.SearchEventsBackgroundTask;
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
=======
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {

    private RecyclerView searchResultsRecyclerView;
    private SearchEventsBackgroundTask searchEventsBackgroundTask;
    private SharedPreferences appSharedPreferences;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
    private SearchResultsAdapter searchAdapter;
    private List<Event> eventsList;
    private TabLayout searchTabLayout;
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
=======
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
=======
    private SearchResultsAdapter searchAdapter;
    private List<Event> eventsList;
    private TabLayout searchTabLayout;
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff

    public SearchResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_search_results, container, false);
        initializeComponents(currentView);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
        startSearch();
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
=======
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
=======
        startSearch();
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
        return currentView;
    }

    private void initializeComponents(View thisView) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
        searchResultsRecyclerView = (RecyclerView) thisView.findViewById(R.id
                .EventSearchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appSharedPreferences = getActivity().getSharedPreferences("iBaleka_Preferences", Context.MODE_PRIVATE);


<<<<<<< HEAD
=======
=======
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
        eventsList = new ArrayList<>();
        searchResultsRecyclerView = (RecyclerView) thisView.findViewById(R.id
                .EventSearchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchTabLayout = (TabLayout) thisView.findViewById(R.id.SearchTabLayout);
        appSharedPreferences = getActivity().getSharedPreferences("iBaleka_DataStore", Context.MODE_PRIVATE);


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
<<<<<<< HEAD
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
=======
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
=======
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
    }

}
