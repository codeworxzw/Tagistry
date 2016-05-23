package com.pk.tagger.realm.artist;

import com.pk.tagger.realm.RealmString;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
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
    private String spotify_id;

    private int spotify_popularity;
    private boolean validated;

    @Ignore
    private RealmList<RealmString> tags;
//    @Ignore
    private RealmList<RealmString> spotify_genre;
    @Ignore
    private RealmList<RealmString> similarArtists;
    @Ignore
    private RealmList<RealmString> currentEvents;

    private ArtistWebsite website;

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

    public RealmList<RealmString> getTags() {
        return tags;
    }

    public void setTags(RealmList<RealmString> tags) {
        this.tags = tags;
    }

    public RealmList<RealmString> getSpotify_genre() {
        return spotify_genre;
    }

    public void setSpotify_genre(RealmList<RealmString> spotify_genre) {
        this.spotify_genre = spotify_genre;
    }

    public RealmList<RealmString> getSimilarArtists() {
        return similarArtists;
    }

    public void setSimilarArtists(RealmList<RealmString> similarArtists) {
        this.similarArtists = similarArtists;
    }

    public RealmList<RealmString> getCurrentEvents() {
        return currentEvents;
    }

    public void setCurrentEvents(RealmList<RealmString> currentEvents) {
        this.currentEvents = currentEvents;
    }

    public String[] getCurrentEventsArray(){
        String[] array = new String [getCurrentEvents().size()];
        for(int i =0; i<array.length; i++){
            array[i] = getCurrentEvents().get(i).getVal();
        }
        return array;
    }

    public String[] getTagsArray(){
        String[] array = new String [getTags().size()];
        for(int i =0; i<array.length; i++){
            array[i] = getTags().get(i).getVal();
        }
        return array;
    }

    public String[] getSpotifyGenreArray(){
        String[] array = new String [getSpotify_genre().size()];
        for(int i =0; i<array.length; i++){
            array[i] = getSpotify_genre().get(i).getVal();
        }
        return array;
    }
}
