package br.com.luizfp.segredosufsc.new_implementation.secret.data;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.secret.Secret;
import io.reactivex.Observable;

/**
 * Created by luiz on 09/02/17.
 */

public interface SecretsDataSource {

    Observable<Response> sendSecret(Secret secret);
}