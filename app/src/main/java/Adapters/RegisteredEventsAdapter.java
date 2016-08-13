package Adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Models.RegisteredEvent;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 7/27/2016.
 */
public class RegisteredEventsAdapter extends RecyclerView.Adapter<RegisteredEventsAdapter.RegisteredEvents> {

    private List<RegisteredEvent> registeredEventList;
    private Activity currentActivity;

    public RegisteredEventsAdapter(Activity currentActivity)
    {
        this.currentActivity = currentActivity;
        registeredEventList = new ArrayList<>();
    }

    public List<RegisteredEvent> getRegisteredEventList() {
        return registeredEventList;
    }

    public void setRegisteredEventList(List<RegisteredEvent> registeredEventList) {
        this.registeredEventList = registeredEventList;
    }

    @Override
    public RegisteredEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        View registeredEventsView = LayoutInflater.from(currentActivity).inflate(R.layout.user_registered_events, parent, false);
        return new RegisteredEvents(registeredEventsView);
    }

    @Override
    public void onBindViewHolder(RegisteredEvents holder, int position) {
        holder.eventTitleTextView.setText(registeredEventList.get(position).getDescription());
        holder.eventTimeTextView.setText(registeredEventList.get(position).getTime());
        holder.eventLocationTextView.setText(registeredEventList.get(position).getLocation());
        holder.eventDateTextView.setText(registeredEventList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return registeredEventList.size();
    }

    class RegisteredEvents extends RecyclerView.ViewHolder {

        private TextView eventTitleTextView;
        private TextView eventDateTextView;
        private TextView eventLocationTextView;
        private TextView eventTimeTextView;
        private CardView eventCardView;

        public RegisteredEvents(View itemView) {
            super(itemView);
            eventTitleTextView = (TextView) itemView.findViewById(R.id.RegisteredEventTitleTextView);
            eventDateTextView = (TextView) itemView.findViewById(R.id.RegisteredEventsDateTextView);
            eventLocationTextView = (TextView) itemView.findViewById(R.id.RegisteredEventsLocationTextView);
            eventTimeTextView = (TextView) itemView.findViewById(R.id.RegisteredEventTimeTextView);
            eventCardView = (CardView) itemView.findViewById(R.id.AthleteRegisteredEventCardView);
        }
    }
}
