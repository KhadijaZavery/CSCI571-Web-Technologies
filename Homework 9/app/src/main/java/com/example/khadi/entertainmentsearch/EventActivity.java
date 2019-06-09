package com.example.khadi.entertainmentsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.StringTokenizer;

import static com.example.khadi.entertainmentsearch.Constants.URL;
import static com.example.khadi.entertainmentsearch.Constants.attractionsArr;
import static com.example.khadi.entertainmentsearch.Constants.category_const;
import static com.example.khadi.entertainmentsearch.Constants.event_id;
import static com.example.khadi.entertainmentsearch.Constants.event_name_constant;
import static com.example.khadi.entertainmentsearch.Constants.sharedpreferences;
import static com.example.khadi.entertainmentsearch.Constants.venue_id;

public class EventActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public String params;
    JSONObject object = null;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private MenuItem favourite;
    private SharedPreferences.Editor editor;
    private String event_name;
    private String datetime;
    private String url;
    private String attractions;
    private String category;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.event_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        final int[] ICONS = new int[]{
                R.drawable.event_icon,
                R.drawable.artist_icon,
                R.drawable.venue_icon,
                R.drawable.upcoming_item
        };

        TabLayout tabLayout = (TabLayout) findViewById(R.id.event_tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
        tabLayout.getTabAt(3).setIcon(ICONS[3]);

        Intent intent = getIntent();
        event_id = intent.getStringExtra("EventId");
        venue_id = intent.getStringExtra("Venue");
        event_name = intent.getStringExtra("EventName");
        event_name_constant = event_name;
        datetime = intent.getStringExtra("Datetime");
        url = intent.getStringExtra("URL");
        attractions = intent.getStringExtra("Attractions");
        category = intent.getStringExtra("Category");
        category_const = category;
        Log.d("Category value is",category);
        String[] split = attractions.split("@");
        attractionsArr.clear();
        for(int i = 0; i < split.length; i++) {
            attractionsArr.add(split[i]);
            //Log.d("artists fetched 123", attractionsArr.get(i));
        }
        //Bundle code not working
        Bundle bundle = new Bundle();
        bundle.putString("my_key", "Hello");
        EventFragment myFrag = new EventFragment();
        myFrag.setArguments(bundle);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(event_name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventActivity.super.onBackPressed();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        favourite = menu.findItem(R.id.favIcon);
        if(sharedpreferences.contains(event_id)){
            favourite.setIcon( ContextCompat.getDrawable(this, R.drawable.heart_fill_red));
        } else {
            favourite.setIcon(ContextCompat.getDrawable(this, R.drawable.heart_fill_white));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("Id of clicked element", String.valueOf(id));
        Log.d("Id of home", String.valueOf(R.id.home));
        if(item.getItemId() ==  R.id.twitterIcon) {

            String text = "Check out " +event_name+" located at "+venue_id +" .Website: "+url;
            String hashTag = "CSCI571EventSearch";
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text="+text
                    +"&url="+url+"&hashtags="+hashTag));
            startActivity(i);

        }
        if(item.getItemId() == R.id.favIcon) {
            editor = sharedpreferences.edit();
            if(sharedpreferences.contains(event_id)) {
                favourite.setIcon(ContextCompat.getDrawable(this, R.drawable.heart_fill_white));
                editor.remove(event_id);
                editor.commit();
                Toast.makeText(getApplicationContext(), event_name+" was removed from favourites",
                        Toast.LENGTH_SHORT).show();

            } else {
                favourite.setIcon(ContextCompat.getDrawable(this, R.drawable.heart_fill_red));
               editor.putString(event_id,event_name+"*"+category+"*"+venue_id+"*"+datetime+"*"+event_id+"*"+attractions);
               editor.commit();
                Toast.makeText(getApplicationContext(), event_name+" was added to favourites",
                        Toast.LENGTH_SHORT).show();
            }
        }
       /* if(item.getItemId() == 16908332)
        {
            Intent myIntent = new Intent(getApplicationContext(), TableActivity.class);
            startActivityForResult(myIntent, 0);
        }*/
        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Position", String.valueOf(position));
            switch (position){
                case 0:
                    EventFragment tab1 = new EventFragment();
                    Log.d("Tab Number","0");
                    return tab1;
                case 1:
                    ArtistsFragment tab2 = new ArtistsFragment();
                    Log.d("Tab Number","1");
                    return tab2;
                case 2:
                    VenueFragment tab3 = new VenueFragment();
                    Log.d("Tab Number","2");
                    return tab3;
                case 3:
                    UpcomingFragment tab4 = new UpcomingFragment();
                    Log.d("Tab Number","3");
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }

}
