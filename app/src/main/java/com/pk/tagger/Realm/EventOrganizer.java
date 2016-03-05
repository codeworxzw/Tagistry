package com.pk.tagger.Realm;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventOrganizer extends RealmObject {

    private String eventOrganizer_ID;
    private String eventOrganizer_Name;

    public EventOrganizer(){}

    public String getEventOrganizer_Name() {
        return eventOrganizer_Name;
    }

    public void setEventOrganizer_Name(String eventOrganizer_Name) {
        this.eventOrganizer_Name = eventOrganizer_Name;
    }

    public String getEventOrganizer_ID() {
        return eventOrganizer_ID;
    }

    public void setEventOrganizer_ID(String eventOrganizer_ID) {
        this.eventOrganizer_ID = eventOrganizer_ID;
    }
}
