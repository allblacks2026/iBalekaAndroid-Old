package Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Models.Event;
import allblacks.com.Activities.R;

/**
 * Created by Okuhle on 7/12/2016.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResult> {

    private List<Event> eventsList;
    private Activity currentActivity;
    private LayoutInflater inflater;

<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
=======
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
    public SearchResultsAdapter(Activity currentActivity) {
        this.currentActivity = currentActivity;
        inflater = LayoutInflater.from(currentActivity);
        eventsList = new ArrayList<>();
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
        notifyItemRangeChanged(0, eventsList.size() -1);
    }


    @Override
    public SearchResult onCreateViewHolder(ViewGroup parent, int viewType) {
        View currentView = inflater.inflate(R.layout.search_result, parent, false);
        return new SearchResult(currentView);
    }

    @Override
    public void onBindViewHolder(SearchResult holder, int position) {
        Event currentEvent = eventsList.get(position);
        holder.eventTitleTextView.setText(currentEvent.getEventDescription());
        holder.eventTimeTextView.setText(currentEvent.getEventTime());
        holder.eventDateTextView.setText(currentEvent.getEventDate());
        holder.locationTextView.setText(currentEvent.getEventLocation());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    class SearchResult extends RecyclerView.ViewHolder {

          private TextView eventTitleTextView;
          private TextView eventDateTextView;
          private TextView eventTimeTextView;
          private TextView locationTextView;


      public SearchResult(View itemView) {
          super(itemView);
          eventTitleTextView = (TextView) itemView.findViewById(R.id.EventSearchTitleTextView);
          eventDateTextView = (TextView) itemView.findViewById(R.id.EventSearchDateTextView);
          eventTimeTextView = (TextView) itemView.findViewById(R.id.EventSearchTimeTextView);
          locationTextView = (TextView) itemView.findViewById(R.id.EventSearchLocationTextView);
      }

<<<<<<< HEAD
<<<<<<< HEAD
=======


>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
=======
>>>>>>> 5676b894da57e86f80ef226bff4e111040e0915a
  }

}

