package br.com.luizfp.segredosufsc.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import br.com.luizfp.segredosufsc.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by luiz on 2/24/16.
 */
public class RestClient {

    /**
     * This is our main backend/server URL.
     */
    public static final String REST_API_URL = "http://192.168.1.11:8070/segredosufscwebservice/rest/";

    private static Retrofit sRetrofit;
    private static OkHttpClient sOkHttpClient;

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        sOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        sOkHttpClient.interceptors().add(logging);

        sRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .baseUrl(REST_API_URL)
                .client(sOkHttpClient)
                .build();
    }

    public static <T> T createService(Class<T> serviceClass) {
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

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.interceptors().add(new AuthorizationInterceptor());
//        builder.addNetworkInterceptor(new StethoInterceptor());
        builder.retryOnConnectionFailure(false);
        // add logging as last interceptor
        builder.interceptors().add(logging);  // <-- this is the important line!
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        return builder.build();
    }
}