package br.com.luizfp.segredosufsc.new_implementation.secret.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.secret.Secret;
import br.com.luizfp.segredosufsc.new_implementation.secret.data.SecretsDataSource;
import io.reactivex.Observable;

/**
 * Created by luiz on 09/02/17.
 */
@Singleton
public final class SecretsRemoteDataSource implements SecretsDataSource {

    @Inject
    public SecretsRemoteDataSource(@NonNull Context context) {

    }

    @Override
    public Observable<Response> sendSecret(Secret secret) {
        return null;
    }
}