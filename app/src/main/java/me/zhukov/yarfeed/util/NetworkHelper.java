package me.zhukov.yarfeed.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Michael Zhukov
 */
public enum NetworkHelper {

    INSTANCE;

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return conMgr.getActiveNetworkInfo() != null &&
                    conMgr.getActiveNetworkInfo().isAvailable() &&
                    conMgr.getActiveNetworkInfo().isConnected();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
