package com.example.khadi.entertainmentsearch;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.khadi.entertainmentsearch.Constants.URL;
import static com.example.khadi.entertainmentsearch.Constants.api_key;
import static com.example.khadi.entertainmentsearch.Constants.attractionsArr;
import static android.support.v7.widget.RecyclerView.VERTICAL;
import static com.example.khadi.entertainmentsearch.Constants.category_const;
import static com.example.khadi.entertainmentsearch.Constants.cx;
import static com.example.khadi.entertainmentsearch.Constants.event_id;
import static com.example.khadi.entertainmentsearch.Constants.event_name_constant;
import static com.example.khadi.entertainmentsearch.Constants.venue_id;

public class ArtistsFragment extends Fragment{
    private ArrayList<String> attractions = new ArrayList<String>();
    private List<ArtistItem> artistsItems;
    private ArtistAdapter artistAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView artistName, popularity, followers, checkAt;
    /*private ArtistItem artistItem;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        attractions = attractionsArr;
        Log.d("Attractions array is", String.valueOf(attractions));
        artistsItems = new ArrayList<>();
        artistAdapter = new ArtistAdapter(getActivity(), artistsItems);
        RecyclerView recyclerViewResults = rootView.findViewById(R.id.rvArtist);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewResults.setHasFixedSize(true);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewResults.setAdapter(artistAdapter);

        if(!attractions.isEmpty())
        {
            getImagesResponse(attractions);
        }
        return rootView;
}

    private void getImagesResponse(final ArrayList<String> attractions) {
        int attractionsLength = attractions.size();
        int i;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        for(i = 0; i < attractionsLength; i++) {
            final ArtistItem artistItem;
            artistItem = new ArtistItem();

            final String attractionName = attractions.get(i);
            if(!category_const.equals(null))
            if(category_const.equals("Music")){
               String spotify_params = "spotifydetails?keyword="+Uri.encode(attractions.get(i));
               String spotifyUrl = URL + spotify_params;
               Log.d("Spotify Url",spotifyUrl);
               StringRequest stringRequest = new StringRequest(Request.Method.GET, spotifyUrl, new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       Log.d("Spotify response",response);
                       try {
                           Log.d("Response used",response);
                           JSONObject spotifyResponse = new JSONObject(response);
                           JSONObject artist = spotifyResponse.getJSONObject("artists")
                                   .getJSONArray("items").getJSONObject(0);
                           artistItem.setName(artist.getString("name"));
                           String followers = NumberFormat.getNumberInstance(Locale.US)
                                   .format(Integer.parseInt(artist.getJSONObject("followers").getString("total")));
                           artistItem.setFollowers(followers);
                           artistItem.setPopularity(artist.getString("popularity"));
                         String url = artist.getJSONObject("external_urls")
                                   .getString("spotify");
                           /*String spotifyLink = "<a href="+url+">Spotify</a>";
                           Log.d("Spotify Link",artistItem.getPopularity());*/
                           artistItem.setCheckAt(url);
                           //artistsItems.add(artistItem);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {

                   }
               });
                requestQueue.add(stringRequest);
            }
            String params = "customsearch?keyword=" +Uri.encode(attractions.get(i))+ "&cx=" +cx+ "&api_key="+api_key;
            String customSearchUrl = URL + params;
            Log.d("artist details URL",customSearchUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, customSearchUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject result = new JSONObject(response);
                        String[] imgUrl = new String[8];
                        for(int j = 0; j < 8; j++) {
                            if(result.getJSONArray("items").getJSONObject(j).has("link")) {
                                imgUrl[j] = result.getJSONArray("items").getJSONObject(j).getString("link");
                                Log.d("Image URL generated"+j,imgUrl[j]);
                            }
                        }
                                artistItem.setImage1(imgUrl[0]);
                                artistItem.setImage2(imgUrl[1]);
                                artistItem.setImage3(imgUrl[2]);
                                artistItem.setImage4(imgUrl[3]);
                                artistItem.setImage5(imgUrl[4]);
                                artistItem.setImage6(imgUrl[5]);
                                artistItem.setImage7(imgUrl[6]);
                                artistItem.setImage8(imgUrl[7]);
                                artistItem.setHeading(attractionName);
                                artistsItems.add(artistItem);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    artistAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error from Google custom search", String.valueOf(error));
                }
            });
            requestQueue.add(stringRequest);
            }
    }
}
