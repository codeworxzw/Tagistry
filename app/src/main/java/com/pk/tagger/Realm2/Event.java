package com.pk.tagger.Realm2;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PK on 16/01/2016.
 */
public class Event extends RealmObject {

    private String eventName;
    private String eventDescription;
    @PrimaryKey
    private String eventID;
    private String eventURL;
    private String eventImageURL;
    private String eventPostedBy;

    private Date eventCreated;
    private Date eventLastUpdated;

    private boolean eventPrivate;


    private EventCategory eventCategory;
    private EventSubCategory eventSubCategory;
    private EventVenue eventVenue;
    private EventOrganizer eventOrganizer;
    private EventStartTime eventStartTime;
    private EventEndTime eventEndTime;


    public Event() { }


    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public EventSubCategory getEventSubCategory() {
        return eventSubCategory;
    }

    public void setEventSubCategory(EventSubCategory eventSubCategory) {
        this.eventSubCategory = eventSubCategory;
    }

    public EventVenue getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(EventVenue eventVenue) {
        this.eventVenue = eventVenue;
    }

    public EventOrganizer getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(EventOrganizer eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public EventStartTime getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(EventStartTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public EventEndTime getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(EventEndTime eventEndTime) {
        this.eventEndTime = eventEndTime;
    }


    public String getEventID() { return eventID; }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventPostedBy() {
        return eventPostedBy;
    }

    public void setEventPostedBy(String eventPostedBy) {
        this.eventPostedBy = eventPostedBy;
    }

    public Date getEventLastUpdated() {
        return eventLastUpdated;
    }

    public void setEventLastUpdated(Date eventLastUpdated) {
        this.eventLastUpdated = eventLastUpdated;
    }

    public boolean getEventPrivate() {
        return eventPrivate;
    }

    public void setEventPrivate(boolean eventPrivate) {
        this.eventPrivate = eventPrivate;
    }

    public String getEventImageURL() {
        return eventImageURL;
    }

    public void setEventImageURL(String eventImageURL) {
        this.eventImageURL = eventImageURL;
    }

    public String getEventURL() {
        return eventURL;
    }

    public void setEventURL(String eventURL) {
        this.eventURL = eventURL;
    }

    public Date getEventCreated() {
        return eventCreated;
    }

    public void setEventCreated(Date eventCreated) {
        this.eventCreated = eventCreated;
    }
}


