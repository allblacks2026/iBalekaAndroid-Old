package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import Listeners.MainActivityListener;
import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartSearchFragment extends Fragment {

    private CheckBox sortResultsCheckBox;
    private EditText searchCriteriaCheckBox;
    private Button searchEventsButton;
    private MainActivityListener buttonListener;
    public StartSearchFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View currentView =  inflater.inflate(R.layout.fragment_start_search, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        searchCriteriaCheckBox = (EditText) currentView.findViewById(R.id.SearchCriteriaEditText);
        sortResultsCheckBox = (CheckBox) currentView.findViewById(R.id.SortByDateCheckBox);
        searchEventsButton = (Button) currentView.findViewById(R.id.SearchEventsButton);
        buttonListener = new MainActivityListener(getActivity());
        searchEventsButton.setOnClickListener(buttonListener);
    }

}
