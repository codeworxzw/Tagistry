package com.pk.tagger.realm;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventPoster extends RealmObject {

    private String id;
    private String name;

    public EventPoster(){}


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
