package br.com.luizfp.segredosufsc.new_implementation.schedulers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Provides different types of schedulers.
 */
@Module
public class SchedulerProvider implements BaseSchedulerProvider {

    @Inject
    public SchedulerProvider() {

    }

    @Override
    @NonNull
    @Provides
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    @NonNull
    @Provides
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    @NonNull
    @Provides
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}