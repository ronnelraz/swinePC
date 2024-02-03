package com.ronnelrazo.physical_counting.connection;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIDownload {
    private static Retrofit retrofit;
    public static APIInterfaceDownload getClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        ConnectionPool connectionPool = new ConnectionPool(10, 10, TimeUnit.MINUTES);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectionPool(connectionPool)
                .connectTimeout(0, TimeUnit.MINUTES)
                .readTimeout(0, TimeUnit.MINUTES);
        httpClient.interceptors().add(logging);
        httpClient.interceptors().add(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Connection", "Keep-Alive")
                    .method(original.method(), original.body())
                    .build();

            Response response = chain.proceed(request);

            if (!response.isSuccessful() || response.code()==503) {
                connectionPool.evictAll();
                response.close();
                return chain.proceed(request);
            } else {
                // Customize or return the response
                return response;
            }
        });
        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(config.downloadURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APIInterfaceDownload d = retrofit.create(APIInterfaceDownload.class);
        return d;
    }
}