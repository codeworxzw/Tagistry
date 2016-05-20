package com.pk.tagger.realm.event;

import com.pk.tagger.realm.RealmString;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.venue.Venue;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PK on 16/01/2016.
 */
public class Event extends RealmObject {


    private String name;
    private String description;
    @PrimaryKey
    private String id;                 //have
    @Ignore
    private RealmList<RealmString> tags;

    private String url;
    private String image_URL;           //have
    private String score;

    private int purchasePrice;     //have

    private Date created;
    private Date lastUpdated;
    private Date date;             //have

    private boolean eventPrivate;
    private boolean validated;

    private EventPoster poster;
    private Artist artist;  //have name, sw_genre_id
    private Venue venue;          //have id, name, location.post_code, location.lng_lat.lng, location.lng_lat.lat
    private EventStartTime startTime;  //have local
    private EventEndTime endTime;
    private EventTickets tickets;      //have purchase_price

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public Event() { }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EventStartTime getStartTime() {
        return startTime;
    }

    public void setStartTime(EventStartTime startTime) {
        this.startTime = startTime;
    }

    public EventEndTime getEndTime() {
        return endTime;
    }

    public void setEndTime(EventEndTime endTime) {
        this.endTime = endTime;
    }


    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isEventPrivate() {
        return eventPrivate;
    }

    public void setEventPrivate(boolean eventPrivate) {
        this.eventPrivate = eventPrivate;
    }
    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public RealmList<RealmString> getTags() {
        return tags;
    }

    public void setTags(RealmList<RealmString> tags) {
        this.tags = tags;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public EventPoster getPoster() {
        return poster;
    }

    public void setPoster(EventPoster poster) {
        this.poster = poster;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public EventTickets getTickets() {
        return tickets;
    }

    public void setTickets(EventTickets tickets) {
        this.tickets = tickets;
    }

    public String[] getTagsArray(){
        String[] array = new String [getTags().size()];
        for(int i =0; i<array.length; i++){
            array[i] = getTags().get(i).getVal();
        }
        return array;
    }


}


