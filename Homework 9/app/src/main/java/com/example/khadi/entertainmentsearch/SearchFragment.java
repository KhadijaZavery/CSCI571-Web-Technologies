package com.example.khadi.entertainmentsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.khadi.entertainmentsearch.Constants.URL;
import static com.example.khadi.entertainmentsearch.Constants.api_key;
import static com.example.khadi.entertainmentsearch.Constants.latitude;
import static com.example.khadi.entertainmentsearch.Constants.longitude;

public class SearchFragment extends Fragment {
    boolean keywordFlag = false, locationFlag = true;
    //private EditText keyword;
    private Spinner category;
    private EditText distance;
    private Spinner distanceUnit;
    private RadioGroup radioGroup;
    private RadioButton radioButtonHere,radioButtonOther;
    private EditText locationText;
    private Button clear;
    private Button search;
    private String params, eventsearch_url, geocoding_url,type, unitType;
    public String encoded;
    private int distance_num;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoCompleteAdapter autoSuggestAdapter;
    private AutoCompleteTextView keyword;
    private String itemPositon;
    //private static final String URL = "https://api.github.com/users";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        //keyword = rootView.findViewById(R.id.keyword);
        category = rootView.findViewById(R.id.category);
        distance = rootView.findViewById(R.id.distance);
        distanceUnit = rootView.findViewById(R.id.distanceUnit);
        radioGroup = rootView.findViewById(R.id.radio_locations);
        radioButtonHere= (RadioButton) rootView.findViewById(R.id.radio_here);
        radioButtonOther = (RadioButton) rootView.findViewById(R.id.radio_other);
        locationText = rootView.findViewById(R.id.locationText);
        search = (Button) rootView.findViewById(R.id.search);
        clear = rootView.findViewById(R.id.clear);
        locationText.setEnabled(false);
        keyword = rootView.findViewById(R.id.keyword);
        autoSuggestAdapter = new AutoCompleteAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        keyword.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        itemPositon = autoSuggestAdapter.getObject(position);
                    }
                });
        keyword.setThreshold(2);
        keyword.setAdapter(autoSuggestAdapter);
        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(keyword.getText())) {
                        Log.d("From autocomplete call",keyword.getText().toString());
                        makeApiCall(keyword.getText().toString());
                    }
                }
                return false;
            }
        });

        radioButtonHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationText.setText("");
                locationText.setEnabled(false);
            }
        });

        radioButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationText.setEnabled(true);

            }
        });

        //Does Validation and if validation passes, then send data to TM.
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //Keyword field is empty
                if(keyword.getText().toString().trim().length() == 0){
                    rootView.findViewById(R.id.errorKeyword).setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Please fix all fields with errors", Toast.LENGTH_SHORT);
                    toast.show();
                    keywordFlag = false;
                }
                else {
                    rootView.findViewById((R.id.errorKeyword)).setVisibility(View.GONE);
                    keywordFlag = true;
                }
                if(radioButtonHere.isChecked()){
                    //Take current location
                    rootView.findViewById(R.id.errorLocation).setVisibility(View.GONE);
                    locationFlag = true;
                }
                if(radioButtonOther.isChecked()){
                    if(locationText.getText().toString().trim().length()==0) {
                        rootView.findViewById(R.id.errorLocation).setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "Please fix all fields with errors", Toast.LENGTH_SHORT);
                        toast.show();
                        locationFlag = false;
                    }
                    else{
                        rootView.findViewById(R.id.errorLocation).setVisibility(View.GONE);
                        locationFlag = true;
                    }
                }
                if(keywordFlag && locationFlag){
                    //Valid data, make the URL for first TM Event search.
                    //If User selects here
                    if(radioButtonHere.isChecked()){
                        //Use current lat and long values
                        params = "current_location?latitude="+latitude+"&longitude="+longitude;
                        String current_url = URL + params;
                        Log.d("current_URL",current_url);
                        getCurrentResponse(current_url);
                    }
                    //If User has typed in custom location
                    if(radioButtonOther.isChecked()) {
                        params = "geocoding?location=" + Uri.encode(locationText.getText().toString()) + "&api_key=" + api_key;
                        geocoding_url = URL + params;
                        Log.d("Before Geocoding sent", geocoding_url);
                        getGeocodeResponse(geocoding_url);
                    }
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword.setText("");
                distance.setText("");
                locationText.setText("");
                radioButtonHere.setChecked(true);
                locationText.setEnabled(false);
                rootView.findViewById(R.id.errorKeyword).setVisibility(View.GONE);
                rootView.findViewById(R.id.errorLocation).setVisibility(View.GONE);
            }
        });
        return rootView;
    }

    public void getCurrentResponse(final String url){
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Current URL",url);
                encoded = response;
                Log.d("Encoded Response - Current",response);
                getEventsearchResponse(encoded);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error",error.toString());
                        Toast.makeText(getActivity().getApplicationContext(),
                                "error",Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    public void getGeocodeResponse(final String url) {
        StringRequest request1 = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Geocoding URL",url);
                encoded = response;
                Log.d("Encoded Response",encoded);
                getEventsearchResponse(encoded);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error",error.toString());
                        Toast.makeText(getActivity().getApplicationContext(),
                                "error",Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request1);
    }

    public void getEventsearchResponse(String encoded) {
        type = category.getSelectedItem().toString();

        if(type.equals("All"))
            type = "";
        if(type.equals("Music"))
            type = "KZFzniwnSyZfZ7v7nJ";
        if(type.equals("Sports"))
            type = "KZFzniwnSyZfZ7v7nE";
        if(type.equals("Arts & Theatre"))
            type = "KZFzniwnSyZfZ7v7na";
        if(type.equals("Film"))
            type = "KZFzniwnSyZfZ7v7nn";
        if(type.equals("Miscellaneous"))
            type = "KZFzniwnSyZfZ7v7n1";

        if( distance.getText().toString().length()==0){
            distance_num = 10;
        } else {
            distance_num = Integer.parseInt(distance.getText().toString());
        }

        unitType = distanceUnit.getSelectedItem().toString();
        if(unitType.equals("Miles"))
            unitType = "miles";
        if(unitType.equals("Kilometers"))
            unitType="km";
        params = "ticketmastersearch_custom?keyword="+Uri.encode(keyword.getText().toString())+
                "&category="+type+"&radius="+distance_num+
                "&unit="+unitType+
                "&encoded="+encoded;
        eventsearch_url = URL + params;
        Log.d("Eventsearch URL sent",eventsearch_url);
        StringRequest request2 = new StringRequest(eventsearch_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Eventsearch URL",eventsearch_url );
                Log.d("Response - eventsearch",response.toString());
                Intent intent = new Intent(getActivity(), TableActivity.class);
                intent.putExtra("EventData", response);
                startActivity(intent);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("URL", eventsearch_url);
                        Log.d("Error from eventsearch",error.toString());
                        Toast.makeText(getActivity().getApplicationContext(),
                                "error",Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request2);
    }

    private void makeApiCall(String text) {
        String url = "https://app.ticketmaster.com/discovery/v2/suggest?apikey=vcMjJX2KAAWHlWzBSe91XzZ3scReRK8c&keyword="+text;
        System.out.println("Auto url "+url);
        StringRequest request1 = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String > stringList = new ArrayList<>();
                try {
                    JSONObject autocomplete_obj = new JSONObject(response);
                    System.out.println("Response auto "+ autocomplete_obj);
                    JSONArray suggestions = autocomplete_obj.getJSONObject("_embedded").getJSONArray("attractions");
                    for (int i=0;i<suggestions.length();i++)
                    {
                        stringList.add(suggestions.getJSONObject(i).getString("name"));
                    }

                } catch (JSONException e) {

                }
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error",error.toString());
                        Toast.makeText(getActivity().getApplicationContext(),
                                "error",Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request1);
    }
}

