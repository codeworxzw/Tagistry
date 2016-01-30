package com.pk.tagger.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pk.tagger.AppController;
import com.pk.tagger.realm.TagData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DatabaseSyncService extends IntentService {

    // temporary string to show the parsed response
    private String jsonResponse;
   // private Realm myRealm;

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


        resetRealm();

        resultReceiver = intent.getParcelableExtra(RESULT_RECEIVER_NAME);

        Bundle resultStarted = new Bundle();
        resultStarted.putString("result", "Service started");
        resultReceiver.send(DATA, resultStarted);

        JsonArrayRequest req = new JsonArrayRequest(QUERY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        Realm myRealm = Realm.getInstance(getApplicationContext());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject event = (JSONObject) response
                                        .get(i);

                                jsonResponse = event.toString();

                                //String message = person.getString("message");
                                //String email = person.getString("email");
                                //JSONObject phone = person
                                  //    .getJSONObject("phone");

                                //jsonResponse += "Message: " + message + "\n\n";
                                //jsonResponse += "Email: " + email + "\n\n";

                                myRealm.beginTransaction();
                                myRealm.createObjectFromJson(TagData.class, event);
                                myRealm.commitTransaction();

                            }


                            Log.d("TAG service", jsonResponse);
                            Bundle getFinished = new Bundle();
                            getFinished.putString("result", jsonResponse);
                            resultReceiver.send(JSONSENT, getFinished);


                            RealmResults<TagData> results1 =
                                    myRealm.where(TagData.class).findAll();

                            for(TagData c:results1) {
                                Log.d("results from realm: ", c.getEventID());    }

                                myRealm.close();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG service", "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


        // we are already in a separate thread here, so we can do some long operation
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }




        Bundle resultFinished = new Bundle();
        resultFinished.putString("result", "Service finished");
        resultReceiver.send(FINISHED, resultFinished);

    }

    private void resetRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.deleteRealm(realmConfig);
    }


}
