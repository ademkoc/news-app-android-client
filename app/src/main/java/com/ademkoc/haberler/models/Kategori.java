package com.ademkoc.haberler.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adem on 8.06.2017.
 */

public class Kategori implements Parcelable {
    private Integer id;
    private String kategoriAdi;
    private boolean isSelected;

    public Kategori() {
    }

    public Kategori(int id, String kategoriAdi) {
        this.id=id;
        this.kategoriAdi=kategoriAdi;
    }

    public Kategori(String kategoriAdi) {
        this.kategoriAdi = kategoriAdi;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKategoriAdi() {
        return this.kategoriAdi;
    }

    public void setKategoriAdi(String kategoriAdi) {
        this.kategoriAdi = kategoriAdi;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.kategoriAdi);
    }

    protected Kategori(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.kategoriAdi = in.readString();
    }

    public static final Parcelable.Creator<Kategori> CREATOR = new Parcelable.Creator<Kategori>() {
        @Override
        public Kategori createFromParcel(Parcel source) {
            return new Kategori(source);
        }

        @Override
        public Kategori[] newArray(int size) {
            return new Kategori[size];
        }
    };
}
