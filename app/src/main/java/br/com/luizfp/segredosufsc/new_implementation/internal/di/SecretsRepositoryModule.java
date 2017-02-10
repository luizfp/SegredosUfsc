package br.com.luizfp.segredosufsc.new_implementation.internal.di;

import javax.inject.Singleton;

import br.com.luizfp.segredosufsc.new_implementation.secret.data.SecretsDataSource;
import br.com.luizfp.segredosufsc.new_implementation.secret.data.SecretsRepository;
import br.com.luizfp.segredosufsc.new_implementation.secret.data.remote.SecretsRemoteDataSource;
import dagger.Binds;
import dagger.Module;

/**
 * This is used by Dagger to inject the required arguments into the {@link SecretsRepository}.
 */
@Module
public abstract class SecretsRepositoryModule {

    @Singleton
    @Binds
    @Remote
    abstract SecretsDataSource provideSecretsRemoteDataSource(
            SecretsRemoteDataSource secretsRemoteDataSource);
}