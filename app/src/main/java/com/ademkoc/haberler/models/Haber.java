package com.ademkoc.haberler.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Adem on 8.06.2017.
 */

public class Haber implements Parcelable {

    private Integer id;
    private Kategori kategori;
    private Long idFromSource;
    private Kaynak kaynak;
    private String haberMetni, imgUrl, haberBaslik;
    private Date tarih;

    public Haber() {
    }

    public Haber(Kategori kategori, Kaynak kaynak, String haberMetni, String imgUrl) {
        this.kategori = kategori;
        this.kaynak = kaynak;
        this.haberMetni = haberMetni;
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Kategori getKategori() {
        return this.kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public Kaynak getKaynak() {
        return this.kaynak;
    }

    public void setKaynak(Kaynak kaynak) {
        this.kaynak = kaynak;
    }

    public String getHaberMetni() {
        return this.haberMetni;
    }

    public void setHaberMetni(String haberMetni) {
        this.haberMetni = haberMetni;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHaberBaslik() {
        return haberBaslik;
    }

    public void setHaberBaslik(String haberBaslik) {
        this.haberBaslik = haberBaslik;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.kategori, flags);
        dest.writeParcelable(this.kaynak, flags);
        dest.writeString(this.haberMetni);
        dest.writeString(this.imgUrl);
        dest.writeString(this.haberBaslik);
        dest.writeLong(this.tarih != null ? this.tarih.getTime() : -1);
    }

    protected Haber(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.kategori = in.readParcelable(Kategori.class.getClassLoader());
        this.kaynak = in.readParcelable(Kaynak.class.getClassLoader());
        this.haberMetni = in.readString();
        this.imgUrl = in.readString();
        this.haberBaslik = in.readString();
        long tmpTarih = in.readLong();
        this.tarih = tmpTarih == -1 ? null : new Date(tmpTarih);
    }

    public static final Parcelable.Creator<Haber> CREATOR = new Parcelable.Creator<Haber>() {
        @Override
        public Haber createFromParcel(Parcel source) {
            return new Haber(source);
        }

        @Override
        public Haber[] newArray(int size) {
            return new Haber[size];
        }
    };

    public Long getIdFromSource() {
        return idFromSource;
    }

    public void setIdFromSource(Long idFromSource) {
        this.idFromSource = idFromSource;
    }
}
