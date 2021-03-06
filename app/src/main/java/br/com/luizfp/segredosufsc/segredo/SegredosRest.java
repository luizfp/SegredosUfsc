package br.com.luizfp.segredosufsc.segredo;

import java.util.List;

import br.com.luizfp.segredosufsc.comentario.Comentario;
import br.com.luizfp.segredosufsc.network.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by luiz on 3/15/16.
 */
public interface SegredosRest {

    @GET("segredos/{idUsuario}")
    Observable<List<Segredo>> getList(@Path("idUsuario") Long idUsuario,
                                      @Query("limit") int limit,
                                      @Query("offset") long offset);

    @POST("segredos")
    Observable<Response> insert(@Body Segredo segredo);

    @GET("segredos/{idUsuario}/comentarios")
    Observable<List<Comentario>> getAllComentsBySegredo(@Path("idUsuario") Long idSegredo);

    @GET("segredos/{idUsuario}/favoritos")
    Observable<List<Segredo>> getListFavorites(@Path("idUsuario") Long idUsuario,
                                                  @Query("limit") int limit,
                                                  @Query("offset") long offset);

    @PUT("segredos")
    Observable<Response> addRemoveFavorite(@Body SegredoFavoritoUsuario segredoFavoritoUsuario);
}
