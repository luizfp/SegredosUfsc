package br.com.luizfp.segredosufsc.new_implementation;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.github.anrwatchdog.ANRWatchDog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;

/**
 * Created by luiz on 2/24/16.
 */
public class SegredosUfscApplication extends android.app.Application {
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        SegredosUfscApplication segredosUfscApplication =
                (SegredosUfscApplication) context.getApplicationContext();
        return segredosUfscApplication.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());
        refWatcher = LeakCanary.install(this);
        new ANRWatchDog().start();
    }
}
