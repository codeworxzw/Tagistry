package com.pk.tagger.RealmTemp;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventEndTime extends RealmObject {
    private String timezone;
    private Date local;
    private Date utc;

    public EventEndTime(){}

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Date getUtc() {
        return utc;
    }

    public void setUtc(Date utc) {
        this.utc = utc;
    }

    public Date getLocal() {
        return local;
    }

    public void setLocal(Date local) {
        this.local = local;
    }
}
