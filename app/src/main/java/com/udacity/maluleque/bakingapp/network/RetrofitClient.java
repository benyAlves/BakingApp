package com.udacity.maluleque.bakingapp.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String API_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static Retrofit retrofit;

    private static Retrofit getInstance(){

        if(retrofit == null ) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(buildOkhttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static BakingService createBakingService() {
        return getInstance().create(BakingService.class);
    }

    private static OkHttpClient buildOkhttpClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();

    }
}
