package com.ankoma88.rateschart.activities;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ankoma88.rateschart.R;
import com.ankoma88.rateschart.fragments.ChartFragment;
import com.ankoma88.rateschart.helpers.DataFetchExecutor;
import com.ankoma88.rateschart.interfaces.DataLoadListener;
import com.ankoma88.rateschart.model.Rate;
import com.ankoma88.rateschart.rest.RestClient;
import com.squareup.okhttp.ResponseBody;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements DataLoadListener, Callback<ResponseBody> {
    public final static String TAG = MainActivity.class.getSimpleName();

    private static final String TAG_CHART_FRAGMENT = "chartFragment";
    public static final int UPDATE_TIME_MILLIS = 1000;

    private ChartFragment fragment;
    private Timer timerExecutor = new Timer();
    private TimerTask dataLoadTask;
    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restClient = new RestClient(this);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        showFragment(savedInstanceState);
    }

    private void showFragment(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fragment = new ChartFragment();

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, fragment, TAG_CHART_FRAGMENT)
                    .commit();
        } else {
            fragment = (ChartFragment) getSupportFragmentManager().findFragmentByTag(TAG_CHART_FRAGMENT);
        }
    }

    public void startDataFetchExecutor() {
        final Handler handler = new Handler();
        dataLoadTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        DataFetchExecutor performBackgroundTask =
                                new DataFetchExecutor(
                                        getApplicationContext());
                        performBackgroundTask.execute(() -> loadRates());
                    } catch (Exception e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
            }
        };
        timerExecutor.schedule(dataLoadTask, 0, UPDATE_TIME_MILLIS);
    }

    private void loadRates() {
        restClient.loadData();

    }

    @Override
    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
        try {
            String data = new String(response.body().bytes());
            Rate rate = new Rate(data);
            if (fragment != null) {
                fragment.onDataLoaded(rate);
            }
        } catch (IOException e) {
            Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
        }

    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataLoadTask.cancel();
        timerExecutor.cancel();
    }

    @Override
    public void loadData() {
        startDataFetchExecutor();
    }
}
