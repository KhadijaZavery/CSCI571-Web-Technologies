package com.example.khadi.entertainmentsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {
    private List<UpcomingItem> items;
    private Context context;
    private LinearLayout llUpcomingItem;

    public UpcomingAdapter(Context context, List<UpcomingItem> items){
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_items, parent, false);
        UpcomingAdapter.UpcomingViewHolder vh = new UpcomingAdapter.UpcomingViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder upcomingViewHolder, int position) {
        UpcomingItem upcomingItem = items.get(position);
        upcomingViewHolder.setEvent(upcomingItem);
        upcomingViewHolder.setListener(upcomingItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class UpcomingViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView artistName;
        public TextView datetime;
        public TextView eventType;

        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.tvEventName);
            artistName = itemView.findViewById(R.id.tvArtistName);
            datetime = itemView.findViewById(R.id.tvDateTime);
            eventType = itemView.findViewById(R.id.tvEventType);
            llUpcomingItem = itemView.findViewById(R.id.llUpcomingItem);
        }

        public void setEvent(UpcomingItem upcomingItem) {
            artistName.setText(upcomingItem.getArtistName());
            eventName.setText(upcomingItem.getEventName());
            eventType.setText(upcomingItem.getEventType());
            datetime.setText(upcomingItem.getDateTime());
        }
        public void setListener(final UpcomingItem upcomingItem) {
            llUpcomingItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = upcomingItem.getUrl();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                }
            });
        }
    }
}
