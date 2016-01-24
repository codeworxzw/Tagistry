package com.pk.tagger.realm;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PK on 16/01/2016.
 */
public class TagData extends RealmObject {

    @PrimaryKey
    private String LatLng;

    private double latitide;
    private double longitude;

    public TagData() { }

    public String getLatLng() {
        return LatLng;
    }

    public void setLatLng(String LatLng) {
        this.LatLng = LatLng;
    }

    public double getLatitide() {
        return latitide;
    }

    public void setLatitide(double latitide) {
        this.latitide = latitide;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}