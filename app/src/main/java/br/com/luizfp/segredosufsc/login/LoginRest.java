package br.com.luizfp.segredosufsc.login;

import java.util.List;

import br.com.luizfp.segredosufsc.Curso;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.network.Credentials;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by luiz on 3/7/16.
 */
public interface LoginRest {

    @FormUrlEncoded
    @POST("login/sendEmailTo")
    Observable<Response> sendEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST("login/confirmAccessCodeAndGenerateToken")
    Observable<Credentials> confirmAccessCodeAndGenerateToken(@Field("idAccessCode") Long idAccessCode,
                                                              @Field("accessCode") String accessCode,
                                                              @Field("letter") char letter,
                                                              @Field("idCourse") Long idCourse);

    @GET("cursos")
    Observable<List<Curso>> getAllCourses();
}
