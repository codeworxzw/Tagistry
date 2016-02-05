package com.pk.tagger.realm;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class LngLat extends RealmObject {

    private long lat;
    private long lng;

    public LngLat(){}
    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

}
