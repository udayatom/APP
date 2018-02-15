package com.uday.controller;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 2017040003 on 26-05-2017.
 */

public class RetrofitClient {



     public static final String BASE_URL = "http://www.test.colive.in/webservice/test/CRMapi/";
    //public static final String BASE_URL = "https://www.colive.in/webservices/CRMapi/";

    private static Retrofit retrofit = null;

    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build();

    public static Retrofit getClient(String URL) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }


}
