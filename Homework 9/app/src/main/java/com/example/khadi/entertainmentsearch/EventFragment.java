package com.example.khadi.entertainmentsearch;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.khadi.entertainmentsearch.Constants.URL;
import static com.example.khadi.entertainmentsearch.Constants.event_id;

public class EventFragment extends Fragment{
    private String params;
    private JSONObject object;
    private TextView artist;
    private TextView venue;
    private TextView time;
    private TextView category;
    private TextView seat;
    private TextView price;
    private TextView status;
    private TextView buyTicket;
    private ProgressBar bar;
    private TextView progressText;
    private LinearLayout events;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String myStr = null;
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        if(getArguments()!=null) {
            Log.d("In getArgs"," ");
            String name=this.getArguments().getString("my_key").toString();
            myStr = getArguments().getString("my_key");
            Log.d("YAAAAAAAY",myStr);
        }
        if(event_id!="")
        {
            String eventDetailsUrl = "";
            params = "eventdetails?id="+event_id;
            eventDetailsUrl = URL + params;
            getEventDetails(eventDetailsUrl);
        }
        artist = rootView.findViewById(R.id.tvArtist);
        venue = rootView.findViewById(R.id.tvVenue);
        time = rootView.findViewById(R.id.tvTime);
        category = rootView.findViewById(R.id.tvCategory);
        price = rootView.findViewById(R.id.tvPrice);
        status = rootView.findViewById(R.id.tvStatus);
        buyTicket = rootView.findViewById(R.id.tvBuy);
        seat = rootView.findViewById(R.id.tvSeatMap);
        bar = (ProgressBar) rootView.findViewById(R.id.progressEvent);
        progressText = rootView.findViewById(R.id.progressTextEvent);
        events = rootView.findViewById(R.id.linearLayout3);
        return rootView;
    }

    private void getEventDetails(final String eventDetailsUrl) {
            String results;
            StringRequest request = new StringRequest(eventDetailsUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Hi Eventdetails URL", eventDetailsUrl );
                    Log.d("Hi Response - eventdetails",response.toString());
                    makeEventDetailsTable(response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Hi Error from eventdetails",error.toString());
                        /*Toast.makeText(,
                                "error",Toast.LENGTH_SHORT).show();*/

                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
    }


    private void makeEventDetailsTable(String response) {
        bar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        events.setVisibility(View.VISIBLE);
        try{
            object = new JSONObject(response);
            //System.out.print(object);
            //JSONObject obj = object.getJSONObject("_embedded");
            //JSONArray events = obj.getJSONArray("events");
            if(object!=null){
                    artist.setText(object.getString("name"));
                    JSONObject obj = object.getJSONObject("_embedded");
                    try {
                        JSONArray venues = obj.getJSONArray("venues");
                        venue.setText(venues.getJSONObject(0).getString("name"));
                    }
                    catch (JSONException e){
                        venue.setText("N/A");
                    }
                    String dateString = object.getJSONObject("dates").getJSONObject("start").getString("localDate");
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
                    String timeString = object.getJSONObject("dates").getJSONObject("start").getString("localTime");
                    time.setText(formattedDate + " "+timeString );
                    try {
                        String segment = object.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                        String genre = object.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
                        category.setText(segment+" | "+genre);
                    }
                    catch (JSONException e)
                    {
                        category.setText("N/A");
                    }

                    try {
                        String min = object.getJSONArray("priceRanges").getJSONObject(0).getString("min");
                        String max = object.getJSONArray("priceRanges").getJSONObject(0).getString("max");
                        price.setText("$" + min + "~ $" + max);
                    }
                    catch (JSONException e){
                        price.setText("N/A");
                    }
                    //Log.d("min",min);
                    try{
                        status.setText(object.getJSONObject("dates").getJSONObject("status").getString("code"));
                    }
                    catch(JSONException e){
                        status.setText("N/A");
                    }
                    try{
                        String map = "<a href="+object.getJSONObject("seatmap").getString("staticUrl")+">View Here</a>";
                        seat.setText(Html.fromHtml(map,Html.FROM_HTML_MODE_LEGACY));
                        seat.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    catch (JSONException e){
                        seat.setText("N/A");
                    }
                    try{
                        String buy = "<a href="+object.getString("url")+">Ticketmaster</a>";
                        buyTicket.setText(Html.fromHtml(buy,Html.FROM_HTML_MODE_LEGACY));
                        buyTicket.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    catch (JSONException e){
                        buyTicket.setText("N/A");
                    }

                        //Log.d("Date", "Got the date: " + formattedDate);
                }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        }
}

