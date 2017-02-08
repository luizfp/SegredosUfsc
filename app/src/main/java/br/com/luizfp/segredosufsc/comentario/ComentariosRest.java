package br.com.luizfp.segredosufsc.comentario;

import br.com.luizfp.segredosufsc.network.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by luiz on 3/17/16.
 */
public interface ComentariosRest {

    @POST("comentarios")
    Observable<Response> insert(@Body Comentario comentario);
}
