package Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import BackgroundTasks.AthleteEventBackgroundTask;
import allblacks.com.iBaleka.R;

public class RegisteredEventsFragment extends Fragment {

    private RecyclerView registeredEventsRecycler;
    private TextView toolbarTextView;
    private SharedPreferences globalPreferences;
    public RegisteredEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView =  inflater.inflate(R.layout.fragment_registered_events, container, false);
        initializeComponents(currentView);
        if (savedInstanceState == null) {
            runTask();
        }
        return currentView;
    }

    private void initializeComponents(View currentView) {
        registeredEventsRecycler = (RecyclerView) currentView.findViewById(R.id.RegisteredEventsRecyclerView);
        registeredEventsRecycler.setLayoutManager(new LinearLayoutManager(currentView.getContext()));
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("View Registered Events");
    }

    private void runTask()
    {
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        AthleteEventBackgroundTask backgroundTask = new AthleteEventBackgroundTask(getActivity());
        backgroundTask.execute(globalPreferences.getString("EmailAddress", ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("View Registered Events");
    }
}
