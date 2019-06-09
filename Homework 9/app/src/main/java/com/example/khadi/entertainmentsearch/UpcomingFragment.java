package com.example.khadi.entertainmentsearch;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static com.example.khadi.entertainmentsearch.Constants.URL;
import static com.example.khadi.entertainmentsearch.Constants.venue_id;

public class UpcomingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private String message = "";
    private List<UpcomingItem> upcomingItems, upcomingItems_copy;
    private LinearLayoutManager linearLayoutManager;
    private UpcomingAdapter adapter;
    private DividerItemDecoration dividerItemDecoration;
    JSONObject object = null;
    String params;
    private int count;
    private Spinner sortCategory, sortOrder;
    private String sortOrderString, sortCategoryString;
    private TextView noRecords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);

        sortCategory = rootView.findViewById(R.id.category_types);
        sortOrder = rootView.findViewById(R.id.sorting_order);
        noRecords = rootView.findViewById(R.id.tvNoRecordsUpcoming);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.category_types, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortCategory.setAdapter(categoryAdapter);
        sortCategory.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.sorting_order, android.R.layout.simple_spinner_item);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOrder.setAdapter(orderAdapter);
        sortOrder.setOnItemSelectedListener(this);
        sortOrderString = "Ascending";
        sortOrder.setEnabled(false);

        upcomingItems = new ArrayList<>();
        upcomingItems_copy = new ArrayList<>();
        adapter = new UpcomingAdapter(getActivity(), upcomingItems);
        RecyclerView recyclerView = rootView.findViewById(R.id.upcoming_data);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        //Make the volley request to Songkick
        if(venue_id!="")
        {
            String songkickSearchUrl = "";
            params = "songkicksearch?venue="+Uri.encode(venue_id);
            songkickSearchUrl = URL + params;
            getSongkickId(songkickSearchUrl);
        }

        return rootView;
}

    private void getSongkickId(String songkickSearchUrl) {
        StringRequest request = new StringRequest(songkickSearchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String songKickId = null;
                try {
                    songKickId = object.getJSONObject("resultsPage").getJSONObject("results")
                            .getJSONArray("venue").getJSONObject(0).getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (songKickId != null && !songKickId.isEmpty() && !songKickId.equals("null"))
                    getSongkickResults(songKickId);
                else{
                    noRecords.setVisibility(View.VISIBLE);
                    sortCategory.setVisibility(GONE);
                    sortOrder.setVisibility(GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error from songkick details",error.toString());

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void getSongkickResults(String id) {
        Log.d("songkick ID details",id.toString());
        String upcomingUrl = "";
        params="songkickupcoming?id="+id;
        upcomingUrl=URL+params;
        StringRequest request = new StringRequest(upcomingUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Hi Response - upcomingdetails",response.toString());
                try {
                    makeUpcomingTable(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Hi Error from songkick",error.toString());
                        /*Toast.makeText(,
                                "error",Toast.LENGTH_SHORT).show();*/

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        
    }

    private void makeUpcomingTable(String response) throws JSONException {

        try {
            object = new JSONObject(response);
            if(object.getJSONObject("resultsPage").getInt("totalEntries") == 0){
                noRecords.setVisibility(View.VISIBLE);
                sortCategory.setVisibility(GONE);
                sortOrder.setVisibility(GONE);
            }
            else{
            JSONArray eventsArray = object.getJSONObject("resultsPage").getJSONObject("results").getJSONArray("event");
            if(eventsArray != null) {
                int len = eventsArray.length();
                if (len >= 5) {
                    count = 5;
                } else {
                    count = len;
                }
                for (int i = 0; i < 5; i++) {
                    UpcomingItem upcomingItem = new UpcomingItem();
                    String displayName = eventsArray.getJSONObject(i).getString("displayName");
                    String artist = eventsArray.getJSONObject(i).getJSONArray("performance").getJSONObject(0).getJSONObject("artist").getString("displayName");
                    String dateString = eventsArray.getJSONObject(i).getJSONObject("start").getString("date");
                    String time = eventsArray.getJSONObject(i).getJSONObject("start").getString("time");
                    String type = eventsArray.getJSONObject(i).getString("type");
                    String url = eventsArray.getJSONObject(i).getString("uri");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                    Date date = null;
                    try {
                        date = sdf.parse(dateString);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM DD, yyyy");
                    String formattedDate = outputFormat.format(date);
                    String datetime = "";
                    if(time.equals("null")||time.equals(null))
                        datetime = formattedDate;
                    else
                        datetime = formattedDate+" "+time;
                    upcomingItem.setDateTime(datetime);
                    upcomingItem.setEventName(displayName);
                    upcomingItem.setArtistName(artist);
                    upcomingItem.setEventType(type);
                    upcomingItem.setUrl(url);
                    upcomingItem.setDate(date);
                    upcomingItems.add(upcomingItem);
                    upcomingItems_copy.add(upcomingItem);
                }
            }
        }
        }
        catch (JSONException e) {
                e.printStackTrace();
            }
        adapter.notifyDataSetChanged();
        }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.category_types && upcomingItems != null) {
            String chosenCategory = parent.getItemAtPosition(position).toString();
            Log.d("Chosen category",chosenCategory);
            if(chosenCategory.equals("Default")) {
                upcomingItems.clear();
                sortOrder.setEnabled(false);
                for(int i = 0; i < upcomingItems_copy.size(); i++) {
                    upcomingItems.add(upcomingItems_copy.get(i));
                }
            }
            else {
                sortOrder.setEnabled(true);
                if(chosenCategory.equals("Event Name")) {
                    Collections.sort(upcomingItems, new Comparator<UpcomingItem>() {
                        @Override
                        public int compare(UpcomingItem o1, UpcomingItem o2) {
                            return o1.getEventName().compareTo(o2.getEventName());
                        }
                    });
                }
                else if(chosenCategory.equals("Artist")) {
                    Collections.sort(upcomingItems, new Comparator<UpcomingItem>() {
                        @Override
                        public int compare(UpcomingItem o1, UpcomingItem o2) {
                            return o1.getArtistName().compareTo(o2.getArtistName());
                        }
                    });
                }
               else if(chosenCategory.equals("Time")) {
                    Collections.sort(upcomingItems, new Comparator<UpcomingItem>() {
                        @Override
                        public int compare(UpcomingItem o1, UpcomingItem o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                }
                else if(chosenCategory.equals("Type")) {
                    Collections.sort(upcomingItems, new Comparator<UpcomingItem>() {
                        @Override
                        public int compare(UpcomingItem o1, UpcomingItem o2) {
                            return o1.getEventType().compareTo(o2.getEventType());
                        }
                    });
                }
                if(sortOrderString.equals("Descending")) {
                    Collections.reverse(upcomingItems);
                }
            }
            adapter.notifyDataSetChanged();
        }
        else if(parent.getId() == R.id.sorting_order && upcomingItems != null) {
            sortOrderString = parent.getItemAtPosition(position).toString();
            Collections.reverse(upcomingItems);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

