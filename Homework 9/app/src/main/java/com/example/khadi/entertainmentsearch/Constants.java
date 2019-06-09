package com.example.khadi.entertainmentsearch;

import android.content.SharedPreferences;

import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

public class Constants {
    //public static String URL="http://10.0.2.2:8080/";
    public static String URL="https://ethereal-app-224018.appspot.com/";
    public static double latitude, longitude;
    //public static String api_key="AIzaSyBYJYNAGMF5MEC-7txG2Hb3xpJBdSsWu6o";
    public static String api_key="AIzaSyDWtGt8DeZ4cWsx69xaXj93eh16rzOUijw";
    public static String event_id= "";
    public static String venue_id="";
    public static String event_name_constant ="";
    public static SharedPreferences sharedpreferences;
    public static String table_message="";
    public static ArrayList<String> attractionsArr = new ArrayList<String>();
    public static String cx ="010016458081968255548:vyeqg66tqwy";
    public static String category_const;
}
