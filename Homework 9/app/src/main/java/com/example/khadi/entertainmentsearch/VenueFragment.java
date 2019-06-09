package com.example.khadi.entertainmentsearch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.view.View.GONE;
import static com.example.khadi.entertainmentsearch.Constants.URL;
import static com.example.khadi.entertainmentsearch.Constants.event_id;
import static com.example.khadi.entertainmentsearch.Constants.venue_id;

public class VenueFragment extends Fragment {
    String params;
    private TextView address;
    private TextView city;
    private TextView phone;
    private TextView openHours;
    private TextView GeneralRule;
    private TextView childRule;
    private TextView venueName;
    private JSONObject object;
    MapView mMapView;
    private GoogleMap googleMap;
    private TextView noRecords;
    private LinearLayout venueLL;
    private LinearLayout addressLL;
    private LinearLayout cityLL;
    private LinearLayout phoneLL;
    private LinearLayout openHoursLL;
    private LinearLayout generalLL;
    private LinearLayout childLL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue, container, false);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        if(venue_id!="")
        {
            String venueDetailsUrl = "";
            params = "venuedetails?keyword="+Uri.encode(venue_id);
            venueDetailsUrl = URL + params;
            getVenueDetails(venueDetailsUrl);
        }
        venueName = rootView.findViewById(R.id.tvName);
        address = rootView.findViewById(R.id.tvAddress);
        city = rootView.findViewById(R.id.tvCity);
        phone = rootView.findViewById(R.id.tvPhone);
        openHours = rootView.findViewById(R.id.tvOpenHours);
        GeneralRule = rootView.findViewById(R.id.tvGeneralRule);
        childRule = rootView.findViewById(R.id.tvChildRule);
        noRecords = rootView.findViewById(R.id.noRecordsVenueTv);
        venueLL = rootView.findViewById(R.id.llName);
        addressLL = rootView.findViewById(R.id.llAddress);
        cityLL = rootView.findViewById(R.id.llCity);
        phoneLL = rootView.findViewById(R.id.llPhone);
        generalLL = rootView.findViewById(R.id.llGeneralRule);
        childLL = rootView.findViewById(R.id.llChildRule);
        openHoursLL = rootView.findViewById(R.id.llOpenHours);

                // For zooming automatically to the location of the marker
        return rootView;


}

    private void getVenueDetails(final String venueDetailsUrl) {
        String results;
        StringRequest request = new StringRequest(venueDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Hi Response - venuedetails",response.toString());
                makeVenueDetailsTable(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Hi Error from venuedetails",error.toString());
                        /*Toast.makeText(,
                                "error",Toast.LENGTH_SHORT).show();*/

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void makeVenueDetailsTable(String response) {
        try {
            createMap(response);
            object = new JSONObject(response);
            Log.d("Venue Response",response);
               if(object.has("_embedded"))
               {
                   try {
                       venueName.setText(object.getJSONObject("_embedded").getJSONArray("venues")
                               .getJSONObject(0).getString("name"));
                   }
                   catch (JSONException e){
                       venueName.setText("N/A");
                   }
                   try{
                       address.setText(object.getJSONObject("_embedded").getJSONArray("venues")
                               .getJSONObject(0).getJSONObject("address").getString("line1"));
                   }
                   catch(JSONException e)
                   {
                       address.setText("N/A");
                   }
                   try {
                       city.setText(object.getJSONObject("_embedded").getJSONArray("venues")
                               .getJSONObject(0).getJSONObject("city").getString("name"));
                   }
                   catch(JSONException e)
                   {
                       city.setText("N/A");
                   }
                   try {
                       phone.setText(object.getJSONObject("_embedded").getJSONArray("venues")
                               .getJSONObject(0).getJSONObject("boxOfficeInfo").getString("phoneNumberDetail"));
                   }
                   catch(JSONException e){
                       phone.setText("N/A");
                   }
                   try {
                       openHours.setText(object.getJSONObject("_embedded").getJSONArray("venues")
                               .getJSONObject(0).getJSONObject("boxOfficeInfo").getString("openHoursDetail"));
                   }
                   catch(JSONException e)
                   {
                       openHours.setText("N/A");
                   }
                   try{
                       GeneralRule.setText(object.getJSONObject("_embedded").getJSONArray("venues")
                               .getJSONObject(0).getJSONObject("generalInfo").getString("generalRule"));
                   }
                   catch(JSONException e)
                   {
                       GeneralRule.setText("N/A");
                   }
                   try{
                       childRule.setText(object.getJSONObject("_embedded").getJSONArray("venues")
                               .getJSONObject(0).getJSONObject("generalInfo").getString("childRule"));
                   }
                   catch (JSONException e){
                       childRule.setText("N/A");
                   }

               }
               else{
                   noRecords.setVisibility(View.VISIBLE);
                   mMapView.setVisibility(View.GONE);
                   venueLL.setVisibility(View.GONE);
                   addressLL.setVisibility(View.GONE);
                   cityLL.setVisibility(View.GONE);
                   phoneLL.setVisibility(View.GONE);
                   generalLL.setVisibility(View.GONE);
                   childLL.setVisibility(View.GONE);
                   openHoursLL.setVisibility(View.GONE);
               }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void createMap(String response) {
       try {
            object = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
              try {
                  if(object.getJSONObject("_embedded").has("venues")) {
                      String lat = object.getJSONObject("_embedded").getJSONArray("venues")
                              .getJSONObject(0).getJSONObject("location").getString("latitude");
                      String lon = object.getJSONObject("_embedded").getJSONArray("venues")
                              .getJSONObject(0).getJSONObject("location").getString("longitude");
                      double lat_double = Double.parseDouble(lat);
                      double lon_double = Double.parseDouble(lon);
                      Log.d("Khadija", lat);
                      LatLng location = new LatLng(lat_double, lon_double);
                      //googleMap.addMarker(new MarkerOptions().position(location));
                      googleMap.addMarker(new MarkerOptions().position(location));
                      CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
                      googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                  }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
