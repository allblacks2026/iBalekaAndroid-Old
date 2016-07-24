package Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapters.AthleteActivityAdapter;
import Models.AthleteActivity;
import allblacks.com.iBaleka.R;

public class UserActivityTabFragment extends Fragment {


    private RecyclerView activityRecyclerView;
    private CardView activityCardView;
    private TextView title, distance, dateDone;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.user_activity_fragment, container, false);
        activityRecyclerView = (RecyclerView) myView.findViewById(R.id.userActivityRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this.getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        activityRecyclerView.setLayoutManager(manager);
        activityCardView = (CardView) myView.findViewById(R.id.userActivityCardView);
        title = (TextView) myView.findViewById(R.id.titleTextView);
        distance = (TextView) myView.findViewById(R.id.totalDistanceTextView);
        dateDone = (TextView) myView.findViewById(R.id.dateDoneTextView);
        List<AthleteActivity> athleteActivity = new ArrayList<>();
        athleteActivity.add(new AthleteActivity("13 January 2016", 5.12, "Sunrise-on-Sea"));
        athleteActivity.add(new AthleteActivity("14 January 2016", 4.22, "Personal Run"));
        athleteActivity.add(new AthleteActivity("18 January 2016", 4.20, "Personal Run"));
        AthleteActivityAdapter adapter = new AthleteActivityAdapter();
        adapter.setAthleteActivityList(athleteActivity);
        activityRecyclerView.setAdapter(adapter);
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
