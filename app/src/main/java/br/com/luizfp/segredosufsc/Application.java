package br.com.luizfp.segredosufsc;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.github.anrwatchdog.ANRWatchDog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;

/**
 * Created by luiz on 2/24/16.
 */
public class Application extends android.app.Application {
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        Application application = (Application) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        refWatcher = LeakCanary.install(this);
        new ANRWatchDog().start();
    }
}
