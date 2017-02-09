package br.com.luizfp.segredosufsc.new_implementation.segredo.data.remote;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.segredo.Secret;
import br.com.luizfp.segredosufsc.new_implementation.segredo.data.SegredosDataSource;
import io.reactivex.Observable;

/**
 * Created by luiz on 09/02/17.
 */

public final class SegredosRemoteDataSource implements SegredosDataSource {

    @Override
    public Observable<Response> sendSecret(Secret secret) {
        return null;
    }
}