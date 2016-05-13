package com.pk.tagger.realm.user;

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

//    private RealmList<RealmString> postedEvents;
//    private RealmList<RealmString> starredEvents;
//    private RealmList<RealmString> starredArtists;
//    private RealmList<RealmString> starredVenues;

    private boolean verified;

}
