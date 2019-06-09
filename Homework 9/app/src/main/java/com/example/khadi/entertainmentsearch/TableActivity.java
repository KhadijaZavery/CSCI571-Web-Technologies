package com.example.khadi.entertainmentsearch;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.khadi.entertainmentsearch.Constants.sharedpreferences;
import static com.example.khadi.entertainmentsearch.Constants.table_message;


public class TableActivity extends AppCompatActivity {
    //private String message = "";
    private RecyclerView recyclerView;
    private List<TableItem> tableItems;
    private LinearLayoutManager linearLayoutManager;
    //private RecyclerView.Adapter adapter;
    private TableAdapter adapter;
    private DividerItemDecoration dividerItemDecoration;
    //private LinearLayoutManager linearLayoutManager;
    JSONObject object = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Search Results");

        Intent intent = getIntent();
        table_message = intent.getStringExtra("EventData");
        Log.d("From TableACtivity",table_message);
        //makeTable();

    }

    private void makeTable() {
        tableItems = new ArrayList<>();
        adapter = new TableAdapter(TableActivity.this, tableItems);
        recyclerView = findViewById(R.id.tableList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try{
                            ProgressBar bar = (ProgressBar) findViewById(R.id.indeterminateBar);
                            bar.setVisibility(View.GONE);
                            TextView progressText = findViewById(R.id.progressText);
                            progressText.setVisibility(View.GONE);
                            object = new JSONObject(table_message);
                            //System.out.print(object);
                            if(object.getJSONObject("page").getInt("totalElements")==0)
                            {
                                TextView noRecordsTv = findViewById(R.id.noRecordsTv);
                                noRecordsTv.setVisibility(View.VISIBLE);
                            }
                            JSONObject obj = object.getJSONObject("_embedded");
                            JSONArray events = obj.getJSONArray("events");
                            if(events!=null){
                                for(int i =0; i<events.length();i++){
                                    JSONObject event = events.getJSONObject(i);
                                    JSONArray venues= event.getJSONObject("_embedded").getJSONArray("venues");
                                    String date = event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                    String time = event.getJSONObject("dates").getJSONObject("start").getString("localTime");
                                    TableItem tableItem = new TableItem();
                                    tableItem.setName(event.getString("name"));
                                    tableItem.setVenue(venues.getJSONObject(0).getString("name"));
                                    //System.out.print((venues.getJSONObject(0).getString("name")));
                                    Log.d("Venue",(venues.getJSONObject(0).getString("name")));
                                    tableItem.setDatetime(date+" "+time);
                                    tableItem.setId(event.getString("id"));
                                    if (sharedpreferences.contains(event.getString("id")))
                                        tableItem.setFavourite(getResources().getDrawable(R.drawable.heart_fill_red, null));
                                    else
                                        tableItem.setFavourite(getResources().getDrawable(R.drawable.heart_outline_black, null));
                                    String category = event.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                                    if(category.equals("Sports"))
                                        tableItem.setImage(getResources().getDrawable(R.drawable.sport_icon,null));
                                    if(category.equals("Music"))
                                        tableItem.setImage(getResources().getDrawable(R.drawable.music_icon,null));
                                    if(category.equals("Arts & Theatre"))
                                        tableItem.setImage(getResources().getDrawable(R.drawable.art_icon,null));
                                    if(category.equals("Miscellaneous"))
                                        tableItem.setImage(getResources().getDrawable(R.drawable.miscellaneous_icon,null));
                                    if(category.equals("Film"))
                                        tableItem.setImage(getResources().getDrawable(R.drawable.film_icon,null));
                                    tableItem.setCategory(category);
                                    tableItem.setUrl(event.getString("url"));
                                    JSONArray attractions = event.getJSONObject("_embedded").getJSONArray("attractions");
                                    int attrLen = attractions.length();
                                    String attractionsList = "";
                                    int j;
                                    for(j = 0; j < attrLen; j++) {
                                        attractionsList+= attractions.getJSONObject(j).getString("name") + "@";
                                    }
                                    tableItem.setAttractions(attractionsList);
                                    tableItems.add(tableItem);
                                }

                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }


                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Change to main Activity
       /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/

       onBackPressed();
        return true;
    }
  @Override
    public void onResume() {
        super.onResume();
        makeTable();
       /* recyclerView = findViewById(R.id.tableList);
        Log.d("table actvity response fav list", String.valueOf(tableItems));
        adapter = new TableAdapter(TableActivity.this,tableItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
*/
    }
}
