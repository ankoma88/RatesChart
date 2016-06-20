package com.ankoma88.rateschart.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.Executor;

/**
 * Created by ankoma88 on 19.06.16.
 */
public class DataFetchExecutor implements Executor {
    private Context context;

    public DataFetchExecutor(Context context) {
        this.context = context;
    }

    @Override public void execute(Runnable command) {
        if (isOnline()) {
            command.run();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
