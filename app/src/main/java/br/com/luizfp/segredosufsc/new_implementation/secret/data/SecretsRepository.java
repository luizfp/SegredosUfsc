package br.com.luizfp.segredosufsc.new_implementation.secret.data;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.Remote;
import br.com.luizfp.segredosufsc.new_implementation.secret.Secret;
import io.reactivex.Observable;

/**
 * Created by luiz on 09/02/17.
 */
@Singleton
public class SecretsRepository implements SecretsDataSource {

    @Inject
    public SecretsRepository(@NonNull @Remote SecretsDataSource secretRemoteDataSource) {

    }

    @Override
    public Observable<Response> sendSecret(Secret secret) {
        return null;
    }
}