package com.example.starwars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.starwars.utils.StarWarsUtils;

import java.text.DecimalFormat;

public class StarWarsDetailActivity extends AppCompatActivity {

    private TextView Name;
    private TextView Detail1;
    private TextView Detail2;
    private TextView Detail3;

    private StarWarsUtils.starwarsItem mstarwarsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

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
        DecimalFormat value = new DecimalFormat("#.#");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String option = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));
        if(option.equals("People")) {
            String Ppl_name = "<b>Character: </b>" + switem.name;
            String Ppl_height = "<b>Height: </b>" + value.format(switem.height * 0.0328084) + " feet";
            String Ppl_mass = "<b>Mass: </b>" + value.format(switem.mass * 2.20462262185) + " lbs";

            Name.setText(Html.fromHtml(Ppl_name));
            Detail1.setText(Html.fromHtml(Ppl_height));
            Detail2.setText(Html.fromHtml(Ppl_mass));
        }
        if (option.equals("Planets")){
            String Plan_name = "<b>Planet: </b>" + switem.name;
            String Plan_climate = "<b>Climate: </b>" + switem.climate;
            String Plan_terrain = "<b>terrain: </b>" + switem.terrain;
            String Plan_pop = "<b>Population: </b>" + switem.Pop;

            Name.setText(Html.fromHtml(Plan_name));
            Detail1.setText(Html.fromHtml(Plan_climate));
            Detail2.setText(Html.fromHtml(Plan_terrain));
            Detail3.setText(Html.fromHtml(Plan_pop));
        }
        if (option.equals("Films")){
            String Film_name = "<b>Title: </b>" + switem.name;
            String Film_direc = "<b>Director: </b>" + switem.director;
            String Film_prod = "<b>Producer: </b>" + switem.producer;

            Name.setText(Html.fromHtml(Film_name));
            Detail1.setText(Html.fromHtml(Film_direc));
            Detail2.setText(Html.fromHtml(Film_prod));

        }
        if (option.equals("Starships")){
            String Ship_name = "<b>Starship: </b>" + switem.name;
            String Ship_model = "<b>Model: </b>" + switem.Model;
            String Ship_manu = "<b>Manufacturer: </b>" + switem.Manufac;
            String Ship_cost = "<b>Cost: </b>" + switem.Cost;

            Name.setText(Html.fromHtml(Ship_name));
            Detail1.setText(Html.fromHtml(Ship_model));
            Detail2.setText(Html.fromHtml(Ship_manu));
            Detail3.setText(Html.fromHtml(Ship_cost));
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