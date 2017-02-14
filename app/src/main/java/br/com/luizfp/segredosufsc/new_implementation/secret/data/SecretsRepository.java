package br.com.luizfp.segredosufsc.new_implementation.secret.data;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.secret.Secret;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

/**
 * Created by luiz on 09/02/17.
 */
@Module
public class SecretsRepository implements SecretsDataSource {

    @Inject
    public SecretsRepository(@NonNull SecretsDataSource secretRemoteDataSource) {

    }

    @Override
    @Provides
    public Observable<Response> sendSecret(Secret secret) {
        return null;
    }
}