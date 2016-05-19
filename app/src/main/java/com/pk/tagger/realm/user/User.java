package com.pk.tagger.realm.user;

import com.pk.tagger.realm.RealmString;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Kieran on 13/05/2016.
 */
public class User extends RealmObject {

    public User () {
    }

    @PrimaryKey
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    private RealmList<RealmString> postedEvents;
    private RealmList<RealmString> starredEvents;
    private RealmList<RealmString> starredArtists;
    private RealmList<RealmString> starredVenues;

    private boolean verified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RealmList<RealmString> getPostedEvents() {
        return postedEvents;
    }

    public void setPostedEvents(RealmList<RealmString> postedEvents) {
        this.postedEvents = postedEvents;
    }

    public RealmList<RealmString> getStarredEvents() {
        return starredEvents;
    }

    public void setStarredEvents(RealmList<RealmString> starredEvents) {
        this.starredEvents = starredEvents;
    }

    public RealmList<RealmString> getStarredArtists() {
        return starredArtists;
    }

    public void setStarredArtists(RealmList<RealmString> starredArtists) {
        this.starredArtists = starredArtists;
    }

    public RealmList<RealmString> getStarredVenues() {
        return starredVenues;
    }

    public void setStarredVenues(RealmList<RealmString> starredVenues) {
        this.starredVenues = starredVenues;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
