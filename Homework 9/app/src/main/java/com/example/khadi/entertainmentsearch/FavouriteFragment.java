package com.example.khadi.entertainmentsearch;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static com.example.khadi.entertainmentsearch.Constants.sharedpreferences;

public class FavouriteFragment extends Fragment{
    private RecyclerView favouriteList;
    private List<TableItem> tableItemList;
    private TextView textView;
    private FavouriteAdapter adapter;
    private Map<String, ?> allEntries;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        favouriteList = rootView.findViewById(R.id.favouritesList);
        textView = rootView.findViewById(R.id.favText);
        makeFavouritesTable();
        return rootView;
    }

    private void makeFavouritesTable() {
        adapter = new FavouriteAdapter(getActivity(), null, null, null);
        allEntries = sharedpreferences.getAll();
        if(allEntries.size()==0) {
            Log.d("Size in if:", String.valueOf(allEntries.size()));
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Favorites");
            textView.setGravity(Gravity.CENTER);
            favouriteList.setVisibility(View.GONE);
        }
        else {
            Log.d("Size in else:", String.valueOf(allEntries.size()));
            tableItemList = new ArrayList<>();
            textView.setVisibility(View.GONE);
            favouriteList.setVisibility(View.VISIBLE);
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

                StringTokenizer tokens = new StringTokenizer(entry.getValue().toString(), "*");
                String eventName = tokens.nextToken();
                String category = tokens.nextToken();
                Log.d("Category obtained",category);
                String venue = tokens.nextToken();
                Log.d("venue obtained",venue);
                String date = tokens.nextToken();
                String id = tokens.nextToken();
                String attractions = tokens.nextToken();
               if(category.equals("Music")) {
                   TableItem tableItem = new TableItem(eventName, getResources().getDrawable(R.drawable.music_icon, null),
                           venue, date, id,
                           ContextCompat.getDrawable(getActivity(), R.drawable.heart_fill_red));
                   tableItem.setAttractions(attractions);
                   tableItem.setCategory(category);
                   tableItemList.add(tableItem);
               }
               if(category.equals("Sports"))
               {
                   TableItem tableItem = new TableItem(eventName, getResources().getDrawable(R.drawable.sport_icon, null),
                           venue, date, id,
                           ContextCompat.getDrawable(getActivity(), R.drawable.heart_fill_red));
                   tableItem.setAttractions(attractions);
                   tableItem.setCategory(category);
                   tableItemList.add(tableItem);
               }
                if(category.equals("Arts & Theatre"))
                {
                    TableItem tableItem = new TableItem(eventName, getResources().getDrawable(R.drawable.sport_icon, null),
                            venue, date, id,
                            ContextCompat.getDrawable(getActivity(), R.drawable.heart_fill_red));
                    tableItem.setAttractions(attractions);
                    tableItem.setCategory(category);
                    tableItemList.add(tableItem);
                }
                if(category.equals("Film"))
                {
                    TableItem tableItem = new TableItem(eventName, getResources().getDrawable(R.drawable.sport_icon, null),
                            venue, date, id,
                            ContextCompat.getDrawable(getActivity(), R.drawable.heart_fill_red));
                    tableItem.setAttractions(attractions);
                    tableItem.setCategory(category);
                    tableItemList.add(tableItem);
                }
                if(category.equals("Miscellaneous"))
                {
                    TableItem tableItem = new TableItem(eventName, getResources().getDrawable(R.drawable.sport_icon, null),
                            venue, date, id,
                            ContextCompat.getDrawable(getActivity(), R.drawable.heart_fill_red));
                    tableItem.setAttractions(attractions);
                    tableItem.setCategory(category);
                    tableItemList.add(tableItem);
                }

            }
            adapter = new FavouriteAdapter(getContext(), tableItemList, textView, favouriteList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

            favouriteList.setLayoutManager(layoutManager);
            favouriteList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //favouriteList = findViewById(R.id.tableList);

       // adapter = new TableAdapter(getContext(),tableItems);
        makeFavouritesTable();
       /* favouriteList.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/

    }
}
