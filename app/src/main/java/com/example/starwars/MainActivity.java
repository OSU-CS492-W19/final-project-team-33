package com.example.starwars;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

    private ArrayList<StarWarsUtils.starwarsItem> mStarWarsItems;
    private static final String SW_URL_KEY = "0wh37sj2msvc73h";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String option = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));

        getSupportActionBar().setElevation(0);

        mStarWarsMainTV = findViewById(R.id.tv_location);
        mStarWarsMainTV.setText(option);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);

        mStarWarsItemsRV = findViewById(R.id.rv_items);
        starWarsAdapter = new StarWarsAdapter(this);
        mStarWarsItemsRV.setAdapter(starWarsAdapter);

        mStarWarsItemsRV.setLayoutManager(new LinearLayoutManager(this ));
        mStarWarsItemsRV.setHasFixedSize(true);

        loadStarWarsInfo();
    }

    public void loadStarWarsInfo(){
        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String option = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));

        String StarWarsURL = StarWarsUtils.buildStarWarsURL(option);
        Log.d(TAG, "Url created: " + StarWarsURL);

        Bundle SWURL = new Bundle();
        SWURL.putString(SW_URL_KEY, StarWarsURL);
        mLoadingIndicatorPB.setVisibility(View.VISIBLE);

        android.support.v4.app.LoaderManager.getInstance(this).initLoader(0,SWURL, this).forceLoad();
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
        String option = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));

        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (s != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mStarWarsItemsRV.setVisibility(View.VISIBLE);
            mStarWarsItems = StarWarsUtils.parseStarWarsJSON(s, option);
            starWarsAdapter.updateStarWarsItems(mStarWarsItems);
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
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
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
