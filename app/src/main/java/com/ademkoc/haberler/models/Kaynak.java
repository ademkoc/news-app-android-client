package com.ademkoc.haberler.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adem on 8.06.2017.
 */

public class Kaynak implements Parcelable {

    private Integer id;
    private String kaynakAdi, imgUrl;

    public Kaynak() {
    }

    public Kaynak(String kaynakAdi, String imgUrl) {
        this.kaynakAdi = kaynakAdi;
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKaynakAdi() {
        return this.kaynakAdi;
    }

    public void setKaynakAdi(String kaynakAdi) {
        this.kaynakAdi = kaynakAdi;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.kaynakAdi);
        dest.writeString(this.imgUrl);
    }

    protected Kaynak(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.kaynakAdi = in.readString();
        this.imgUrl = in.readString();
    }

    public static final Parcelable.Creator<Kaynak> CREATOR = new Parcelable.Creator<Kaynak>() {
        @Override
        public Kaynak createFromParcel(Parcel source) {
            return new Kaynak(source);
        }

        @Override
        public Kaynak[] newArray(int size) {
            return new Kaynak[size];
        }
    };
}

