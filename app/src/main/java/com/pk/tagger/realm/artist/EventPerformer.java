package com.pk.tagger.realm.artist;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventPerformer extends RealmObject {

    private String id;
    private String name;
    private String image_URL;
    //private String[] sw_genre;
    private String sw_genre_id;
    private String sw_website;
    //private String[] tags;
    private String website;
    private String spotify_id;
    //private String[] spotify_genre;

    private int spotify_popularity;

    //private SimilarArtist<RealmList> similarArtists;

    public EventPerformer(){
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

//    public String[] getSw_genre() {
//        return sw_genre;
//    }
//
//    public void setSw_genre(String[] sw_genre) {
//        this.sw_genre = sw_genre;
//    }

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

//    public String[] getTags() {
//        return tags;
//    }
//
//    public void setTags(String[] tags) {
//        this.tags = tags;
//    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSpotify_id() {
        return spotify_id;
    }

    public void setSpotify_id(String spotify_id) {
        this.spotify_id = spotify_id;
    }

//    public String[] getSpotify_genre() {
//        return spotify_genre;
//    }
//
//    public void setSpotify_genre(String[] spotify_genre) {
//        this.spotify_genre = spotify_genre;
//    }

    public int getSpotify_popularity() {
        return spotify_popularity;
    }

    public void setSpotify_popularity(int spotify_popularity) {
        this.spotify_popularity = spotify_popularity;
    }

//    public SimilarArtist getSimilarArtists() {
//        return similarArtists;
//    }
//
//    public void setSimilarArtists(SimilarArtist similarArtists) {
//        this.similarArtists = similarArtists;
//    }




}
