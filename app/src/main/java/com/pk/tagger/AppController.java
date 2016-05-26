package com.pk.tagger;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Kieran on 27/01/2016.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("gigit.realm")
                //.schemaVersion(0)
                //.migration(new MyRealmMigration()) // Migration to run instead of throwing an exception
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        // Don't forget to set your fb app id in strings.xml!
        FacebookSdk.sdkInitialize(this);

        // initialize stormpath
        StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
                .baseUrl("https://gigitch.duckdns.org") //"https://stormpathnotes.herokuapp.com"
                .build();
        Stormpath.init(this, stormpathConfiguration);

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
