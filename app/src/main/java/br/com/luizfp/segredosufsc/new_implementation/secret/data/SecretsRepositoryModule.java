package br.com.luizfp.segredosufsc.new_implementation.secret.data;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import br.com.luizfp.segredosufsc.new_implementation.internal.di.Remote;
import br.com.luizfp.segredosufsc.new_implementation.secret.data.remote.SecretsRemoteDataSource;
import dagger.Module;
import dagger.Provides;

/**
 * This is used by Dagger to inject the required arguments into the {@link SecretsRepository}.
 */
@Module
class SecretsRepositoryModule {

    @Singleton
    @Provides
    @Remote
    SecretsDataSource provideSecretsRemoteDataSource(@NonNull Context context) {
        return new SecretsRemoteDataSource(context);
    }
}