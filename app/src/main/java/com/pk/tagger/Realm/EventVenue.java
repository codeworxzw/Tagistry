package com.pk.tagger.Realm;

import io.realm.RealmObject;

/**
 * Created by Kobi on 08/02/2016.
 */
public class EventVenue extends RealmObject{

    private String eventVenue_Name;
    private String eventVenue_ID;

    private EventVenue_Location eventVenue_Location;

    public EventVenue(){};

    public EventVenue_Location getEventVenue_Location() {
        return eventVenue_Location;
    }

    public void setEventVenue_Location(EventVenue_Location eventVenue_Location) {
        this.eventVenue_Location = eventVenue_Location;
    }

    public String getEventVenue_Name() {
        return eventVenue_Name;
    }

    public void setEventVenue_Name(String eventVenue_Name) {
        this.eventVenue_Name = eventVenue_Name;
    }

    public String getEventVenue_ID() {
        return eventVenue_ID;
    }

    public void setEventVenue_ID(String eventVenue_ID) {
        this.eventVenue_ID = eventVenue_ID;
    }

}
