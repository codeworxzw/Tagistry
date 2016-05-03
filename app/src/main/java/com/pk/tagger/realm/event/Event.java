package com.pk.tagger.realm.event;

import com.pk.tagger.realm.artist.EventPerformer;
import com.pk.tagger.realm.venue.EventVenue;

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
    private String eventID;                 //have
    //private String[] eventTags;
    private String eventURL;
    private String eventImageURL;           //have
    private String eventScore;

    private int eventPurchasePrice;     //have

    private Date eventCreated;
    private Date eventLastUpdated;
    private Date eventDate;             //have

    private boolean eventPrivate;

    private EventPoster eventPoster;
    private EventPerformer eventPerformer;  //have name, sw_genre_id
    private EventVenue eventVenue;          //have id, name, location.post_code, location.lng_lat.lng, location.lng_lat.lat
    private EventStartTime eventStartTime;  //have local
    private EventEndTime eventEndTime;
    private EventTickets eventTickets;      //have purchase_price

    public Event() { }

    public EventVenue getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(EventVenue eventVenue) {
        this.eventVenue = eventVenue;
    }

    public int getEventPurchasePrice() {
        return eventPurchasePrice;
    }

    public void setEventPurchasePrice(int eventPurchasePrice) {
        this.eventPurchasePrice = eventPurchasePrice;
    }
    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
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

//    public String[] getEventTags() {
//        return eventTags;
//    }
//
//    public void setEventTags(String[] eventTags) {
//        this.eventTags = eventTags;
//    }

    public String getEventScore() {
        return eventScore;
    }

    public void setEventScore(String eventScore) {
        this.eventScore = eventScore;
    }

    public boolean isEventPrivate() {
        return eventPrivate;
    }

    public EventPoster getEventPoster() {
        return eventPoster;
    }

    public void setEventPoster(EventPoster eventPoster) {
        this.eventPoster = eventPoster;
    }

    public EventPerformer getEventPerformer() {
        return eventPerformer;
    }

    public void setEventPerformer(EventPerformer eventPerformer) {
        this.eventPerformer = eventPerformer;
    }

    public EventTickets getEventTickets() {
        return eventTickets;
    }

    public void setEventTickets(EventTickets eventTickets) {
        this.eventTickets = eventTickets;
    }
//    public Date getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(Date startTime) {
//        this.startTime = startTime;
//    }

}


