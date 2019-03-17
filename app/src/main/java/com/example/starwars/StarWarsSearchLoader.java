package com.example.starwars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import android.util.Log;

import com.example.starwars.utils.NetworkUtils;

import java.io.IOException;


public class StarWarsSearchLoader extends AsyncTaskLoader<String>{

    private static final String TAG = StarWarsSearchLoader.class.getSimpleName();

    private String mStarWarsAdapter;
    private String mStarWarsSearchURL;

    public StarWarsSearchLoader(@NonNull Context context , String url) {
        super(context);
        mStarWarsSearchURL = url;
    }

    @Override
    protected void onStartLoading() {
        if (mStarWarsSearchURL != null) {
            if (mStarWarsAdapter != null) {
                Log.d(TAG, "Loading cached results");
                deliverResult(mStarWarsAdapter);
            } else {
                forceLoad();
            }
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        if (mStarWarsSearchURL != null) {
            String searchResults = null;
            try {
                Log.d(TAG, "Loading results from StarWars with URL: " + mStarWarsSearchURL);
                searchResults = NetworkUtils.doHTTPGet(mStarWarsSearchURL);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        } else {
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable String data) {
        mStarWarsAdapter = data;
        super.deliverResult(data);
    }
}
