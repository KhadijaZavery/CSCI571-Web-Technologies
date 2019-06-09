package com.example.khadi.entertainmentsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.List;
import java.util.Map;

import static com.example.khadi.entertainmentsearch.Constants.sharedpreferences;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    private List<TableItem> items;
    private Context context;
    private LinearLayout llTableItem;

    public TableAdapter(Context context, List<TableItem> items){
        this.items = items;
        this.context = context;
    }
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_items, parent, false);
        TableViewHolder vh = new TableViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder tableViewHolder, int position) {
        TableItem tableItem = items.get(position);
        tableViewHolder.setEvent(tableItem);
        tableViewHolder.setListenerRowId(tableItem);
        //tableViewHolder.setListener(tableItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView name;
        public TextView venue;
        public TextView datetime;
        public ImageView favourite;
        private SharedPreferences.Editor editor;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.event_name);
            venue = itemView.findViewById(R.id.venue);
            datetime = itemView.findViewById(R.id.datetime);
            favourite = itemView.findViewById(R.id.favourite);
            llTableItem = itemView.findViewById(R.id.llTableItem);
        }

        public void setEvent(TableItem tableItem) {
            name.setText(tableItem.getName());
            venue.setText(tableItem.getVenue());
            favourite.setImageDrawable(tableItem.getFavourite());
            datetime.setText(tableItem.getDatetime());
            image.setImageDrawable(tableItem.getImage());
            editor = sharedpreferences.edit();
        }


        public void setListenerRowId(final TableItem tableItem) {
            final Drawable redHeartImage = ContextCompat.getDrawable(context, R.drawable.heart_fill_red);
            final Drawable blackOutlineImage = ContextCompat.getDrawable(context,R.drawable.heart_outline_black);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor = sharedpreferences.edit();
                    if(sharedpreferences.contains(tableItem.getId())){
                        Map<String, ?> allEntries = sharedpreferences.getAll();
                        editor.remove(tableItem.getId());
                        editor.commit();
                        favourite.setImageDrawable(blackOutlineImage);
                        Toast.makeText(context, name.getText()+" was removed from favourites",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, ?> allEntries = sharedpreferences.getAll();
                        editor.putString(tableItem.getId(), tableItem.getName()+"*"+tableItem.getCategory()+"*"
                                +tableItem.getVenue()+"*"+tableItem.getDatetime()+"*"+tableItem.getId()+"*"+tableItem.getAttractions());
                        editor.commit();
                        favourite.setImageDrawable(redHeartImage);
                        Toast.makeText(context, name.getText()+" was added to favourites",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            llTableItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                String eventId = tableItem.getId();
                Log.d("Getting event ID",eventId);
                Intent intent = new Intent(v.getContext(),EventActivity.class);
                intent.putExtra("EventId", eventId);

                String venueName = tableItem.getVenue();
                intent.putExtra("Venue", venueName);

                String eventName = tableItem.getName();
                intent.putExtra("EventName",eventName);
                intent.putExtra("Datetime",tableItem.getDatetime());
                intent.putExtra("URL",tableItem.getUrl());
                intent.putExtra("Category",tableItem.getCategory());
                intent.putExtra("Attractions",tableItem.getAttractions());
                context.startActivity(intent);
                }
            });
        }
    }
}
