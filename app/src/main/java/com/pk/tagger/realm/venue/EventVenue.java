package com.pk.tagger.realm.venue;

import io.realm.RealmObject;

/**
 * Created by Kobi on 08/02/2016.
 */
public class EventVenue extends RealmObject{

    private String name;
    private String id;
    private String website;
    private String sw_website;
    private String image_URL;

    private EventVenue_Location location;

    public EventVenue(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSw_website() {
        return sw_website;
    }

    public void setSw_website(String sw_website) {
        this.sw_website = sw_website;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public EventVenue_Location getLocation() {
        return location;
    }

    public void setLocation(EventVenue_Location location) {
        this.location = location;
    }




}
