package br.com.luizfp.segredosufsc.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by luiz on 2/24/16.
 */
public class RestClient {

    /**
     * This is our main backend/server URL.
     */
    public static final String REST_API_URL = "http://192.168.1.19:8080/segredosufscwebservice/rest/";

    private static Retrofit sRetrofit;
    private static  OkHttpClient sOkHttpClient;

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        sOkHttpClient = new OkHttpClient();
        sOkHttpClient.interceptors().add(logging);
        sOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        sOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);

        sRetrofit = new retrofit.Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .baseUrl(REST_API_URL)
                .client(sOkHttpClient)
                .build();
    }

    public static <T> T createService(Class<T> serviceClass) {
        sOkHttpClient.interceptors().clear();
        return createService(serviceClass, null);
    }

    public static <T> T createService(Class<T> serviceClass, Credentials credentials) {
        if (credentials != null) {
            sOkHttpClient.interceptors().add(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization",
                                credentials.getTokenType() + " " + credentials.getAccessToken())
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        }

        return sRetrofit.create(serviceClass);
    }


    /*
     * ========================================================
     * PRIVATE METHODS
     * ========================================================
     */
    private static Gson getGson()  {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setPrettyPrinting()
                .serializeSpecialFloatingPointValues()
                .create();
    }
}