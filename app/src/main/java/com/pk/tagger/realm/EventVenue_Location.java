package com.pk.tagger.realm;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventVenue_Location extends RealmObject {
    private String PostCode;
    private String City;
    private String Address_1;

    private LngLat LngLat;

    public EventVenue_Location(){}

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress_1() {
        return Address_1;
    }

    public void setAddress_1(String address_1) {
        Address_1 = address_1;
    }

    public com.pk.tagger.realm.LngLat getLngLat() {
        return LngLat;
    }

    public void setLngLat(com.pk.tagger.realm.LngLat lngLat) {
        LngLat = lngLat;
    }

}
