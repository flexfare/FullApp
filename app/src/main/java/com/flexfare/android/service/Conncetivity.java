package com.flexfare.android.service;

        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;

/**
 * Created by kodenerd on 8/22/17.
 */
@SuppressWarnings("deprecation")
public class Conncetivity {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}