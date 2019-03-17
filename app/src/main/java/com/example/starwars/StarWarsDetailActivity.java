package com.example.starwars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.starwars.utils.StarWarsUtils;

public class StarWarsDetailActivity extends AppCompatActivity {

    private TextView Name;
    private TextView Detail1;
    private TextView Detail2;
    private TextView Detail3;



    private StarWarsUtils.starwarsItem mstarwarsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_item_detail);

        Name = (TextView) findViewById(R.id.tv_name);
        Detail1 = (TextView) findViewById(R.id.tv_Detail1);
        Detail2 = (TextView) findViewById(R.id.tv_Detail2);
        Detail3 = (TextView) findViewById(R.id.tv_Detail3);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(StarWarsUtils.starwarsItem.EXTRA_ITEM)) {
            mstarwarsItem = (StarWarsUtils.starwarsItem) intent.getSerializableExtra(StarWarsUtils.starwarsItem.EXTRA_ITEM);
            fillInLayoutText(mstarwarsItem);
        }
    }

    private void fillInLayoutText(StarWarsUtils.starwarsItem switem) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String option = sharedPreferences.getString(getString(R.string.pref_cata_key), getString(R.string.pref_cata_default));
        if(option.equals("People")) {
            String Name2 = "<b>Character: </b>" + switem.name;
            String CharHeihgt = "<b>Height: </b>" + switem.height;
            String CharMsas = "<b>Mass: </b>" + switem.mass;

            Name.setText(Html.fromHtml(Name2));
            Detail1.setText(Html.fromHtml(CharHeihgt));
            Detail2.setText(Html.fromHtml(CharMsas));
        }
        if (option.equals("Planets")){
            String Name2 = "<b>Planet: </b>" + switem.name;
            String Clim = "<b>Climate: </b>" + switem.climate;
            String Terra = "<b>terrain: </b>" + switem.terrain;
            String Pop = "<b>Population: </b>" + switem.Popul;

            Name.setText(Html.fromHtml(Name2));
            Detail1.setText(Html.fromHtml(Clim));
            Detail2.setText(Html.fromHtml(Terra));
            Detail3.setText(Html.fromHtml(Pop));
        }
        if (option.equals("Films")){
            String Name2 = "<b>Title: </b>" + switem.name;
            String dir = "<b>Director: </b>" + switem.director;
            String prod = "<b>Producer: </b>" + switem.producer;

            Name.setText(Html.fromHtml(Name2));
            Detail2.setText(Html.fromHtml(dir));
            Detail3.setText(Html.fromHtml(prod));

        }
        if (option.equals("Starships")){
            String shipName = "<b>Vehicle Name: </b>" + switem.name;
            String shipModel = "<b>Model: </b>" + switem.ShipModel;
            String shipManu = "<b>Manufacturer: </b>" + switem.ShipManu;
            String shipCost = "<b>Cost: </b>" + switem.ShipCost + " Credits";

            Name.setText(Html.fromHtml(shipName));
            Detail1.setText(Html.fromHtml(shipModel));
            Detail2.setText(Html.fromHtml(shipManu));
            Detail3.setText(Html.fromHtml(shipCost));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_repo:
                viewRepoOnWeb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repo_detail, menu);
        return true;
    }

    public void viewRepoOnWeb() {
        if (mstarwarsItem != null) {
            String url = "http://www.starwars.com";
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse(url));
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }
    }

}