package com.pk.tagger.realm.event;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventStartTime extends RealmObject {
    private String timezone;
    private Date local;
    private Date utc;
    public EventStartTime(){}

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Date getLocal() {
        return local;
    }

    public void setLocal(Date local) {
        this.local = local;
    }

    public Date getUtc() {
        return utc;
    }

    public void setUtc(Date utc) {
        this.utc = utc;
    }
}
