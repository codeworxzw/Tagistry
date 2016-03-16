package com.pk.tagger.realm;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class SimilarArtist<RealmList> extends RealmObject {

    private String id;
    private String name;
    private String spotify_id;

    public SimilarArtist(){}

    public String getSpotify_id() {
        return spotify_id;
    }

    public void setSpotify_id(String spotify_id) {
        this.spotify_id = spotify_id;
    }

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
}
