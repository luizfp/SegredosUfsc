package br.com.luizfp.segredosufsc.new_implementation.internal.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import br.com.luizfp.segredosufsc.new_implementation.secret.data.SecretsDataSource;
import br.com.luizfp.segredosufsc.new_implementation.secret.data.SecretsRepository;
import br.com.luizfp.segredosufsc.new_implementation.secret.data.remote.SecretsRemoteDataSource;
import dagger.Module;
import dagger.Provides;

/**
 * This is used by Dagger to inject the required arguments into the {@link SecretsRepository}.
 */
@Module
public class SecretsRepositoryModule {

    public SecretsRepositoryModule() {}

    @Provides
    SecretsDataSource provideSecretsRemoteDataSource(@NonNull Context context) {
        return new SecretsRemoteDataSource(context);
    }
}