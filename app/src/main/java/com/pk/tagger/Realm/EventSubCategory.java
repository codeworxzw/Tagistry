package com.pk.tagger.Realm;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventSubCategory extends RealmObject {

    private String eventSubCategory_Name;
    private String eventSubCategory_ID;

    public EventSubCategory(){}

    public String getEventSubCategory_Name() {
        return eventSubCategory_Name;
    }

    public void setEventSubCategory_Name(String eventSubCategory_Name) {
        this.eventSubCategory_Name = eventSubCategory_Name;
    }


    public String getEventSubCategory_ID() {
        return eventSubCategory_ID;
    }

    public void setEventSubCategory_ID(String eventSubCategory_ID) {
        this.eventSubCategory_ID = eventSubCategory_ID;
    }
}
