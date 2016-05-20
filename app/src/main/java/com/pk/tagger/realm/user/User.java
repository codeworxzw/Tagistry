package com.pk.tagger.realm.user;

import android.util.Log;

import com.pk.tagger.realm.RealmString;

import java.util.Arrays;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
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

    public String[] getStarredArtistsArray(){
        String[] array = new String [getStarredArtists().size()];
        for(int i =0; i<array.length; i++){
            array[i] = getStarredArtists().get(i).getVal();
        }
        return array;
    }

    public String[] getStarredEventsArray(){
        String[] array = new String [getStarredEvents().size()];
        for(int i =0; i<array.length; i++){
            array[i] = getStarredEvents().get(i).getVal();
        }
        return array;
    }

    public String[] getStarredVenuesArray(){
        String[] array = new String [getStarredVenues().size()];
        for(int i =0; i<array.length; i++){
            array[i] = getStarredVenues().get(i).getVal();
        }
        return array;
    }

    public String[] getPostedEventsArray(){
        String[] array = new String [getPostedEvents().size()];
        for(int i =0; i<array.length; i++){
            array[i] = getPostedEvents().get(i).getVal();
        }
        return array;
    }

    public void addPostedEvent(String id){

        if (Arrays.asList(getPostedEventsArray()).contains(id)){
            Log.d("User", "Already posted event: " +id);
        } else {
            RealmString starred = new RealmString();
            starred.setVal(id);
            this.postedEvents.add(starred);
            Log.d("User", "Added posted event: " +id);

        }
    }

    public void addStarredEvent(String id){

        if (Arrays.asList(getStarredEventsArray()).contains(id)){
            Log.d("User", "Already starred event: " +id);
        } else {
            RealmString starred = new RealmString();
            starred.setVal(id);
            this.starredEvents.add(starred);
            Log.d("User", "Added starred event: " +id);

        }
    }

    public void addStarredArtist (String id){

        if (Arrays.asList(getStarredArtistsArray()).contains(id)){
            Log.d("User", "Already starred artist: " +id);
        } else {
            RealmString starred = new RealmString();
            starred.setVal(id);
            this.starredArtists.add(starred);
            Log.d("User", "Added starred artist: " +id);

        }
    }

    public void addStarredVenue (String id){

        if (Arrays.asList(getStarredVenuesArray()).contains(id)){
            Log.d("User", "Already starred venue: " +id);
        } else {
            RealmString starred = new RealmString();
            starred.setVal(id);
            this.starredVenues.add(starred);
            Log.d("User", "Added starred venue: " +id);

        }
    }

    public void removeStarredEvent (String id) {
        if (Arrays.asList(getStarredEventsArray()).contains(id)){
            int index = Arrays.asList(getStarredEventsArray()).indexOf(id);
            starredEvents.remove(index);
            Log.d("User", "Removed starred event: " +id);
        } else {
            Log.d("User", "Could not find starred event: " +id);
        }
    }

    public void removeStarredArtist (String id) {
        if (Arrays.asList(getStarredArtistsArray()).contains(id)){
            int index = Arrays.asList(getStarredArtistsArray()).indexOf(id);
            starredArtists.remove(index);
            Log.d("User", "Removed starred artist: " +id);
        } else {
            Log.d("User", "Could not find starred artist: " +id);
        }
    }

    public void removeStarredVenue (String id) {
        if (Arrays.asList(getStarredVenuesArray()).contains(id)){
            int index = Arrays.asList(getStarredVenuesArray()).indexOf(id);
            starredVenues.remove(index);
            Log.d("User", "Removed starred venue: " +id);
        } else {
            Log.d("User", "Could not find starred venue: " +id);
        }
    }

    public void removePostedEvent (String id) {
        if (Arrays.asList(getPostedEventsArray()).contains(id)){
            int index = Arrays.asList(getPostedEventsArray()).indexOf(id);
            postedEvents.remove(index);
            Log.d("User", "Removed posted event: " +id);
        } else {
            Log.d("User", "Could not find posted event: " +id);
        }
    }
}
