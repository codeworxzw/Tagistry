package com.pk.tagger.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pk.tagger.restclient.EventRestClient;
import com.pk.tagger.realm.event.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmException;

public class DatabaseSyncService extends IntentService {

    // temporary string to show the parsed response
    private String jsonResponse;
    private Realm myRealm;

    private static final String QUERY_URL = "http://52.31.31.106:9000/apiunsecure/events";

    public static final String RESULT_RECEIVER_NAME = "DatabaseSyncReceiver";

    public static final int DATA = 0;
    public static final int FINISHED = 1;
    public static final int JSONSENT = 2;

    private ResultReceiver resultReceiver;

    public DatabaseSyncService() {
        super("DatabaseSyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        resultReceiver = intent.getParcelableExtra(RESULT_RECEIVER_NAME);

        Bundle resultStarted = new Bundle();
        resultStarted.putString("result", "Service started");
        resultReceiver.send(DATA, resultStarted);

        SharedPreferences sharedPreferences = getSharedPreferences("TimeStamp", Context.MODE_PRIVATE);
        Date date = new Date(System.currentTimeMillis());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("time", date.getTime());
        editor.commit();

        Date myDate = new Date(sharedPreferences.getLong("time", 0));

        Log.d("Date in DSyncService", myDate.toString());
        myRealm = Realm.getDefaultInstance();

        EventRestClient.get("", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("JSONObject", response.toString());

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {

               // Log.d("JSONArray", data.toString());

                try {
                    // Parsing json array response
                    // loop through each json object
                    jsonResponse = "";
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject event = (JSONObject) data
                                .get(i);

                        jsonResponse = event.toString();

                        //Log.d("jsonResponse", jsonResponse);
                        try{
                            myRealm.beginTransaction();
                            myRealm.createOrUpdateObjectFromJson(Event.class, event);
                            myRealm.commitTransaction();
                        } catch (RealmException e) {
                            e.printStackTrace();
                            myRealm.cancelTransaction();
                        }

                        // we are already in a separate thread here, so we can do some long operation

                        //try {
                          //  Thread.sleep(50);
                       // } catch (InterruptedException e) {
                       // }


                    }


                    // Log.d("TAG service", jsonResponse);
                    //  Bundle getFinished = new Bundle();
                    //  getFinished.putString("result", jsonResponse);
                    //  resultReceiver.send(JSONSENT, getFinished);


/*                   RealmResults<Event> results1 =
                           myRealm.where(Event.class).findAll();

                   for(Event c:results1) {
                       Log.d("Realm EventLngLats: ", c.getVenue().getLocation().getLng_lat().toString());
                       Log.d("Realm EventVenueName: ", c.getVenue().getName().toString());
                       Log.d("Realm EventName: ", c.getName().toString());
                       Log.d("Realm EventStartTime: ", c.getStartTime().toString());
                       Log.d("Realm EventID: ", c.getId().toString());
                   } */



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        myRealm.close();

        Bundle resultFinished = new Bundle();
        resultFinished.putString("result", "Service finished");
        resultReceiver.send(FINISHED, resultFinished);

    }

}
