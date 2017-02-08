package br.com.luizfp.segredosufsc.network;

import br.com.luizfp.segredosufsc.Usuario;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by luiz on 3/15/16.
 */
public interface UsuarioRest {

    @PUT("usuarios")
    Observable<Response> updateInicial(@Body Usuario usuario);
}
