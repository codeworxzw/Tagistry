package com.pk.tagger.realm;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class LngLat extends RealmObject {

    private double lat;
    private double lng;

    public LngLat(){}
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

}
