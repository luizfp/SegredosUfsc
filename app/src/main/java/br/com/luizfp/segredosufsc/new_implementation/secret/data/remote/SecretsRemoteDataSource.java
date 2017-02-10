package br.com.luizfp.segredosufsc.new_implementation.secret.data.remote;

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
    public SecretsRemoteDataSource() {

    }

    @Override
    public Observable<Response> sendSecret(Secret secret) {
        return null;
    }
}