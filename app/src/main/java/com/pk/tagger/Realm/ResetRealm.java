package com.pk.tagger.Realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by PK on 26/02/2016.
 */
public class ResetRealm {

    public static void resetRealm(Context context) {
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(context.getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.deleteRealm(realmConfig);
    }



}


