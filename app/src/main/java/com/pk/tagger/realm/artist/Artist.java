package com.pk.tagger.realm.artist;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by uk on 05/02/2016.
 */
public class Artist extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String description;
    private String image_URL;
    private String sw_genre;
    private String sw_genre_id;
    private String sw_website;
    //private String[] tags;
    private ArtistWebsite website;
    private String spotify_id;
    //private String[] spotify_genre;

    private int spotify_popularity;

    //private String[] currentEvents;

    private boolean validated;

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSw_genre() {
        return sw_genre;
    }

    public void setSw_genre(String sw_genre) {
        this.sw_genre = sw_genre;
    }

    public ArtistWebsite getWebsite() {
        return website;
    }

    public void setWebsite(ArtistWebsite website) {
        this.website = website;
    }

//    public String[] getCurrentEvents() {
//        return currentEvents;
//    }
//
//    public void setCurrentEvents(String[] currentEvents) {
//        this.currentEvents = currentEvents;
//    }

    //private SimilarArtist<RealmList> similarArtists;

    public Artist(){
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }


    public String getSw_genre_id() {
        return sw_genre_id;
    }

    public void setSw_genre_id(String sw_genre_id) {
        this.sw_genre_id = sw_genre_id;
    }

    public String getSw_website() {
        return sw_website;
    }

    public void setSw_website(String sw_website) {
        this.sw_website = sw_website;
    }


    public String getSpotify_id() {
        return spotify_id;
    }

    public void setSpotify_id(String spotify_id) {
        this.spotify_id = spotify_id;
    }

    public int getSpotify_popularity() {
        return spotify_popularity;
    }

    public void setSpotify_popularity(int spotify_popularity) {
        this.spotify_popularity = spotify_popularity;
    }

}
