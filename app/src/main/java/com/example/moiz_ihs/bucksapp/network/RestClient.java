package com.example.moiz_ihs.bucksapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Moiz-IHS on 7/28/2018.
 */

public class RestClient {
    private static final int TIMEOUT = 120;
    public static final String BASE_URL = "http://bucksapi.appbuild.me/public/api/";
    private static ApiInterface restClient;



    static {
        setupClient();
    }

    private static void setupClient() {

        setupRestClient();
    }



    private static void setupRestClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest;
                        newRequest = request.newBuilder()
                                .addHeader("Accept", "application/json")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })*/.connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                        writeTimeout(TIMEOUT, TimeUnit.SECONDS).
                        readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)

                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                client(httpClient).
                build();

        restClient = retrofit.create(ApiInterface.class);
    }


    public static ApiInterface getAuthAdapter(){
        return restClient;
    }

}
