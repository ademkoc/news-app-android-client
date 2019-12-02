package com.ademkoc.haberler.rest;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Adem on 12.06.2017.
 */

public class ApiClient {

    private static final String mBaseUrl = "http://192.168.43.188:8080/gazete-ws/rest/";
    private Retrofit mRetrofit = null;

    public Retrofit getClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}