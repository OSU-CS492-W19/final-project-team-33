package com.example.starwars.utils;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.starwars.R;
import java.io.Serializable;
import java.util.ArrayList;


public class StarWarsUtils {
    private final static String Base_URL = "https://swapi.co/api";

    public static class starwarsItem implements Serializable{
        public static final String EXTRA_ITEM = "com.example.starwars.utils.starwarsItem.SearchResult";

        public String name;
        public double height;
        public double mass;

        public String climate;
        public String terrain;
        public String Pop;

        public String title;
        public String director;
        public String producer;

        public String Model;
        public String Manufac;
        public String Cost;
    }

    public static String buildStarWarsURL(String option){
        String CATEGORY = "people";

        if(option.equals("People")){
            CATEGORY = "people";
        } else if(option.equals("Films")){
            CATEGORY = "films";
        }else if(option.equals("Planets")){
            CATEGORY = "planets";
        }else if(option.equals("Starships")){
            CATEGORY = "starships";
        }

        return Uri.parse(Base_URL).buildUpon()
                .appendPath(CATEGORY)
                .toString();
    }

    public static ArrayList<starwarsItem> parseStarWarsJSON(String StarWarsJSON, String option){
        try{
            JSONObject StarWarsOBJ = new JSONObject(StarWarsJSON);
            JSONArray StarWarsList;
            StarWarsList = StarWarsOBJ.getJSONArray("results");

            ArrayList<starwarsItem> starwarsItemsList = new ArrayList<starwarsItem>();
            for (int i = 0; i< StarWarsList.length(); i++){
                starwarsItem starwarsItem = new starwarsItem();
                JSONObject StarWarsListElem = StarWarsList.getJSONObject(i);

                if(option.equals("People")) {
                    starwarsItem.name = StarWarsListElem.getString("name");
                    starwarsItem.height = StarWarsListElem.getInt("height");
                    starwarsItem.mass = StarWarsListElem.getInt("mass");
                }

                else if(option.equals("Films")) {
                    starwarsItem.name = StarWarsListElem.getString("title");
                    starwarsItem.director = StarWarsListElem.getString("director");
                    starwarsItem.producer = StarWarsListElem.getString("producer");
                }

                else if(option.equals("Starships")) {
                    starwarsItem.name = StarWarsListElem.getString("name");
                    starwarsItem.Model = StarWarsListElem.getString("model");
                    starwarsItem.Manufac = StarWarsListElem.getString("manufacturer");
                    starwarsItem.Cost = StarWarsListElem.getString("cost_in_credits");
                }

                else if(option.equals("Planets")) {
                    starwarsItem.name = StarWarsListElem.getString("name");
                    starwarsItem.climate = StarWarsListElem.getString("climate");
                    starwarsItem.terrain = StarWarsListElem.getString("terrain");
                    starwarsItem.Pop = StarWarsListElem.getString("population");
                }
                starwarsItemsList.add(starwarsItem);
            }
            return starwarsItemsList;
        }
        catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
