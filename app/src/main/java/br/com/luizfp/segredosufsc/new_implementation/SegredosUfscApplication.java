package br.com.luizfp.segredosufsc.new_implementation;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.github.anrwatchdog.ANRWatchDog;
import com.squareup.leakcanary.LeakCanary;

import br.com.luizfp.segredosufsc.BuildConfig;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.component.ApplicationComponent;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.module.ApplicationModule;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.component.SecretsRepositoryComponent;
import io.fabric.sdk.android.Fabric;

/**
 * Created by luiz on 2/24/16.
 */
public class SegredosUfscApplication extends android.app.Application {

    private ApplicationComponent mApplicationComponent;
    private SecretsRepositoryComponent mSecretsRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initFabric();
        initLeakCanary();
        initWatchDog();
        initInjector();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public SecretsRepositoryComponent getSecretsRepositoyComponent() {
        return mSecretsRepositoryComponent;
    }

    private void initFabric() {
        Fabric.with(this, new Crashlytics(), new Answers());
    }

    private void initWatchDog() {
        new ANRWatchDog().start();
    }

    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }

    private void initInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(SegredosUfscApplication.this))
                .build();
        mSecretsRepositoryComponent = DaggerSecretsRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(SegredosUfscApplication.this))
                .build();
    }
}