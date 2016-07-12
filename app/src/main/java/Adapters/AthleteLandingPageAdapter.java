package Adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Fragments.AthleteLandingFragment;
import Models.Athlete;
import Models.AthleteData;
import allblacks.com.Activities.R;

/**
 * Created by Okuhle on 7/5/2016.
 */
public class AthleteLandingPageAdapter extends RecyclerView.Adapter<AthleteLandingPageAdapter.AthleteViewHolder> {

    private List<AthleteData> athleteDataList;
    private Activity currentActivity;

    public AthleteLandingPageAdapter(Activity currentActivity) {
     this.currentActivity = currentActivity;
    }


    @Override
    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .athlete_landing_screen_card, parent, false);
        return new AthleteViewHolder(myView);
    }

    public List<AthleteData> getAthleteDataList() {
        return athleteDataList;
    }

    public void setAthleteDataList(List<AthleteData> athleteDataList) {
        this.athleteDataList = athleteDataList;
    }

    @Override
    public void onBindViewHolder(AthleteViewHolder holder, int position) {
        holder.titleTextView.setText(athleteDataList.get(position).getAthleteDataTitle());
        holder.dataValueTextView.setText(athleteDataList.get(position).getAthleteDataValue());
        holder.dataValueTextView.setText(athleteDataList.get(position).getAthleteDataFooter());
    }
    @Override
    public int getItemCount() {
        return athleteDataList.size();
    }
    public class AthleteViewHolder extends RecyclerView.ViewHolder {

        private CardView dataCard;
        private TextView titleTextView;
        private TextView dataValueTextView;
        private TextView footerTextView;

        public AthleteViewHolder(View itemView) {
            super(itemView);
            dataCard = (CardView) itemView.findViewById(R.id.LandingScreenCardView);
            titleTextView = (TextView) itemView.findViewById(R.id.LandingScreenAttributeHeading);
            dataValueTextView = (TextView) itemView.findViewById(R.id.LandingScreenDataLabel);
            footerTextView = (TextView) itemView.findViewById(R.id.LandingScreeAttributeTwo);
        }
    }
}
