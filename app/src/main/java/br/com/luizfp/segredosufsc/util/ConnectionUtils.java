package br.com.luizfp.segredosufsc.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by luiz on 3/7/16.
 */
public class ConnectionUtils {

    /**
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
