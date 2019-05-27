package com.yudhapn.moviecatalogue.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkReceiver extends BroadcastReceiver {
    private NetworkCallback callback;
    public NetworkReceiver(NetworkCallback callback) {
        this.callback = callback;
    }

    public NetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            callback.onConnected();
        } else {
            callback.onDisconnected();
        }
    }

    public interface NetworkCallback {
        void onConnected();
        void onDisconnected();
    }
}
