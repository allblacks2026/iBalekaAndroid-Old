package Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Models.AthleteActivity;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 4/3/2016.
 */
public class AthleteActivityAdapter extends RecyclerView.Adapter<AthleteActivityAdapter.AthleteViewHolder> {

    private List<AthleteActivity> athleteActivityList;
    public List<AthleteActivity> getAthleteActivityList()
    {
        return athleteActivityList;
    }

    public void setAthleteActivityList (List<AthleteActivity> athleteActivityList)
    {
        this.athleteActivityList = athleteActivityList;
    }

    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .user_activity_card, parent, false);
        return new AthleteViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(AthleteViewHolder holder, int position) {
        holder.title.setText(athleteActivityList.get(position).getTitle());
        holder.distanceCovered.setText(Double.toString(athleteActivityList.get(position)
                .getDistanceCovered()));
        holder.dateCovered.setText(athleteActivityList.get(position).getDateCovered());
    }

    @Override
    public int getItemCount() {
        return athleteActivityList.size();
    }

    class AthleteViewHolder extends RecyclerView.ViewHolder {

        private CardView activityCardView;
        private TextView title, dateCovered, distanceCovered;

        public AthleteViewHolder(View itemView) {
            super(itemView);
            activityCardView = (CardView) itemView.findViewById(R.id.userActivityCardView);
            title = (TextView) itemView.findViewById(R.id.titleTextView);
            dateCovered = (TextView) itemView.findViewById(R.id.dateDoneTextView);
            distanceCovered = (TextView) itemView.findViewById(R.id.totalDistanceTextView);
        }
    }
}
