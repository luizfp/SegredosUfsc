package br.com.luizfp.segredosufsc.network;

import br.com.luizfp.segredosufsc.Usuario;
import retrofit.http.Body;
import retrofit.http.PUT;
import rx.Observable;

/**
 * Created by luiz on 3/15/16.
 */
public interface UsuarioRest {

    @PUT("usuarios")
    Observable<Response> updateInicial(@Body Usuario usuario);
}
