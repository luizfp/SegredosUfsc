package br.com.luizfp.segredosufsc.comentario;

import br.com.luizfp.segredosufsc.network.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by luiz on 3/17/16.
 */
public interface ComentariosRest {

    @POST("comentarios")
    Observable<Response> insert(@Body Comentario comentario);
}
