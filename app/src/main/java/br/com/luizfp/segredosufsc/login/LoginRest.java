package br.com.luizfp.segredosufsc.login;

import java.util.List;

import br.com.luizfp.segredosufsc.Curso;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.network.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
