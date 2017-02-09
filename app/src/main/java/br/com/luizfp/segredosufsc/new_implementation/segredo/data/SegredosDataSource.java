package br.com.luizfp.segredosufsc.new_implementation.segredo.data;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.segredo.Secret;
import io.reactivex.Observable;

/**
 * Created by luiz on 09/02/17.
 */

public interface SegredosDataSource {

    Observable<Response> sendSecret(Secret secret);
}