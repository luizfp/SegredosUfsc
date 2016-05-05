package br.com.luizfp.segredosufsc.util;

import android.util.Log;

import br.com.luizfp.segredosufsc.BuildConfig;

/**
 * Created by luiz on 2/27/16.
 */
public class L {
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }
}
