package com.ademkoc.haberler.models;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Adem on 10.06.2017.
 */

public class Kullanici {

    private static final String TAG = Kullanici.class.getSimpleName();
    private Integer id;
    private String isim, eposta, token;

    public Kullanici() {
    }

    public Kullanici(String isim, String eposta, String token) {
        this.isim=isim;
        this.eposta=eposta;
        this.token=token;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsim() {
        return this.isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getEposta() {
        return this.eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
