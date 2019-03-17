package com.example.starwars;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.preference.PreferenceManager;
//import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.starwars.utils.StarWarsUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StarWarsAdapter.OnStarWarsItemClickListener, LoaderManager.LoaderCallbacks<String>{

    private TextView mStarWarsMainTV;
    private RecyclerView mStarWarsItemsRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private StarWarsAdapter starWarsAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private ArrayList<StarWarsUtils.starwarsItem> mStarWarsStuff;
    private static final String SW_URL_KEY = "urlKey";
    private static final int LOADER_ID = 0;
    private static final int SW_SEARCH_LOADER_ID = 0;
    ArrayList<StarWarsUtils.starwarsItem> starwarsItems;
    private static final String LIFECYCLE_EVENTS_TEXT_KEY = "lifecycleEvents";
    private static final String REPOS_ARRAY_KEY = "WeatherRepos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener(){

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                refreshDisplay();
            }
        };
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String option = sharedPreferences.getString(getString(R.string.pref_cata_key), getString(R.string.pref_default));

        getSupportActionBar().setElevation(0);

        mStarWarsMainTV = findViewById(R.id.tv_forecast_location);
        mStarWarsMainTV.setText(option);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);

        mStarWarsItemsRV = findViewById(R.id.rv_forecast_items);
        starWarsAdapter = new StarWarsAdapter(this);
        mStarWarsItemsRV.setAdapter(starWarsAdapter);

        if(savedInstanceState != null && savedInstanceState.containsKey(LIFECYCLE_EVENTS_TEXT_KEY)){
            starwarsItems = (ArrayList<StarWarsUtils.starwarsItem>) savedInstanceState.getSerializable(REPOS_ARRAY_KEY);
            starWarsAdapter.updateStarWarsItems(starwarsItems);
        }

        getSupportLoaderManager().initLoader(SW_SEARCH_LOADER_ID, null, this);

        mStarWarsItemsRV.setLayoutManager(new LinearLayoutManager(this ));
        mStarWarsItemsRV.setHasFixedSize(true);

        loadStarWars();

        Log.d("Refresh", "Called Refresh Display");
    }

    public void refreshDisplay(){

        getSupportActionBar().setElevation(0);

        mStarWarsMainTV = findViewById(R.id.tv_forecast_location);
        mStarWarsMainTV.setText("Characters");

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);

        mStarWarsItemsRV = findViewById(R.id.rv_forecast_items);
        starWarsAdapter = new StarWarsAdapter(this);
        mStarWarsItemsRV.setAdapter(starWarsAdapter);

        getSupportLoaderManager().initLoader(SW_SEARCH_LOADER_ID, null, this);

        mStarWarsItemsRV.setLayoutManager(new LinearLayoutManager(this ));
        mStarWarsItemsRV.setHasFixedSize(true);

        loadStarWars();
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        Log.d("Refresh", "Called Refresh Display");
        loadStarWars();
    }


    public void loadStarWars(){
        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String option = sharedPreferences.getString(getString(R.string.pref_cata_key), getString(R.string.pref_default));

        String StarWarsURL = StarWarsUtils.buildStarWarsURL(option);
        Log.d(TAG, "Built the url for " + StarWarsURL);

        Bundle SWURL = new Bundle();
        SWURL.putString(SW_URL_KEY, StarWarsURL);
        mLoadingIndicatorPB.setVisibility(View.VISIBLE);

        getSupportLoaderManager().restartLoader(LOADER_ID, SWURL, this);
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle bundle) {
        String url = null;
        if (bundle != null) {
            url = bundle.getString(SW_URL_KEY);
        }
        return new StarWarsSearchLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String option = sharedPreferences.getString(getString(R.string.pref_cata_key), getString(R.string.pref_default));

        Log.d("Finished", "Cached JSON data loaded");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (s != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mStarWarsItemsRV.setVisibility(View.VISIBLE);
            mStarWarsStuff = StarWarsUtils.parseStarWarsJSON(s, option);
            starWarsAdapter.updateStarWarsItems(mStarWarsStuff);
        } else {
            mStarWarsItemsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        //NOTHING
    }

    @Override
    public void onStarWarsItemClick(StarWarsUtils.starwarsItem starwarsItem) {
        Intent intent = new Intent(this, StarWarsDetailActivity.class);

        intent.putExtra(StarWarsUtils.starwarsItem.EXTRA_ITEM, starwarsItem);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPreferences(){
        Intent preferences = new Intent(this, SettingsActivity.class);
        startActivity(preferences);
    }
}
