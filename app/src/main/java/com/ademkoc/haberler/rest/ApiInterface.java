package com.ademkoc.haberler.rest;

import com.ademkoc.haberler.models.Haber;
import com.ademkoc.haberler.models.Kategori;
import com.ademkoc.haberler.models.Kullanici;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Adem on 12.06.2017.
 */

public interface ApiInterface {
    @GET("main/getKategoriler")
    Call<List<Kategori>> getCategorys();

    @GET("main/getHaberler")
    Call<List<Haber>> getNews(@Query("id") List<Integer> newsIds);

    @POST("main/sendUser")
    Call<Void> sendUser(@Body Kullanici kullanici);
}