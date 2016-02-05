package com.pk.tagger.realm;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventCategory extends RealmObject {

    private String eventCategory_ID;
    private String eventCategory_Name;

    public EventCategory(){}

    public String getEventCategory_Name() {
        return eventCategory_Name;
    }

    public void setEventCategory_Name(String eventCategory_Name) {
        this.eventCategory_Name = eventCategory_Name;
    }

    public String getEventCategory_ID() {
        return eventCategory_ID;
    }

    public void setEventCategory_ID(String eventCategory_ID) {
        this.eventCategory_ID = eventCategory_ID;
    }
}
