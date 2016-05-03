package com.pk.tagger.realm.venue;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventVenue_Location extends RealmObject {
    private String post_code;
    private String city;
    private String address_1;

    private String address_2;

    private LngLat lng_lat;

    public EventVenue_Location(){}

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public LngLat getLng_lat() {
        return lng_lat;
    }

    public void setLng_lat(LngLat lng_lat) {
        this.lng_lat = lng_lat;
    }

    public String getAddress_2() { return address_2; }

    public void setAddress_2(String address_2) { this.address_2 = address_2; }

}
