package com.pk.tagger.realm;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PK on 16/01/2016.
 */
public class TagData extends RealmObject {

    @PrimaryKey
    private String eventID;

    private String eventTitle;
    private String eventDescription;
    private String eventCategory;
    private String eventPostedBy;

    private Date eventDate;
    private Date eventLastUpdate;

    private double eventPrice;



    private double eventLatitude;
    private double eventLongitude;

    public TagData() { }


    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventPostedBy() {
        return eventPostedBy;
    }

    public void setEventPostedBy(String eventPostedBy) {
        this.eventPostedBy = eventPostedBy;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getEventLastUpdate() {
        return eventLastUpdate;
    }

    public void setEventLastUpdate(Date eventLastUpdate) {
        this.eventLastUpdate = eventLastUpdate;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public double getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(double eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public double getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(double eventLongitude) {
        this.eventLongitude = eventLongitude;
    }
}