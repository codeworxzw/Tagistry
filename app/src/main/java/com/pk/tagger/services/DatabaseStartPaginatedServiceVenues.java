package com.pk.tagger.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pk.tagger.realm.CustomGsonBuilder;
import com.pk.tagger.realm.venue.Venue;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DatabaseStartPaginatedServiceVenues extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DOWNLOAD = "com.pk.tagger.services.action.FOO";
    private static final String ACTION_UPDATE = "com.pk.tagger.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.pk.tagger.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.pk.tagger.services.extra.PARAM2";

    // temporary string to show the parsed response
    private String jsonResponse;
    private Realm myRealm;



    private int pageCount = 1;       //total page count (initialize as 1, then update from response)

    public DatabaseStartPaginatedServiceVenues() {
        super("DatabaseStartService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDownload(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DatabaseStartPaginatedServiceVenues.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);

    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionUpdate(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DatabaseStartPaginatedServiceVenues.class);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDownload(param1, param2);
            } else if (ACTION_UPDATE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionUpdate(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownload(String param1, String param2) {
        // this needs to be kept if new database schema is being used

        SharedPreferences sharedPreferences = getSharedPreferences("TimeStamp", Context.MODE_PRIVATE);
        Date date = new Date(System.currentTimeMillis());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("time", date.getTime());
        editor.commit();

        Date myDate = new Date(sharedPreferences.getLong("time", 0));

        Log.d("Date in DStartService", myDate.toString());
//        myRealm = Realm.getDefaultInstance();
//        myRealm.beginTransaction();

        for(int j = 1; j<=pageCount; j++ ) {
            Log.d("pageNumber", Integer.toString(j));

        /*    VenuesRestClient.get("/" + j, null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    //Log.d("JSONObject", "JSONObject received");

                    try {
                        // Parsing json array response
                        // loop through each json object
                        //jsonResponse = "";
                        pageCount = response.getInt("pages");
                        Log.d("Page Count", Integer.toString(pageCount));

                        JSONArray docs = (JSONArray) response.get("docs");
                        Log.d("Docs.get(2)", docs.get(2).toString());

                        for (int i = 0; i < docs.length(); i++) {

                            JSONObject venue = (JSONObject) docs.get(i);

                            //jsonResponse = event.toString();

                            //Log.d("jsonResponse", jsonResponse);
                           // myRealm.beginTransaction();
                            myRealm.createOrUpdateObjectFromJson(Venue.class, venue);
                           // myRealm.commitTransaction();

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


//                   RealmResults<Venue> results1 =
//                           myRealm.where(Venue.class).findAll();
//
//                   for(Venue c:results1) {
//                       Log.d("Realm VenueLngLats: ", c.getLocation().getLng_lat().toString());
//                       Log.d("Realm VenueName: ", c.getName());
//                       //Log.d("Realm EventName: ", c.getName().toString());
//                       //Log.d("Realm EventStartTime: ", c.getStartTime().toString());
//                       Log.d("Realm VenueID: ", c.getId());
//                   }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
//
//                    Log.d("JSONArray", "JSONArray received");
//
//                    try {
//                        // Parsing json array response
//                        // loop through each json object
//                        jsonResponse = "";
//                        for (int i = 0; i < data.length(); i++) {
//
//                            JSONObject event = (JSONObject) data
//                                    .get(i);
//
//                            jsonResponse = event.toString();
//
//                            //Log.d("jsonResponse", jsonResponse);
//                            myRealm.beginTransaction();
//                            myRealm.createObjectFromJson(Event.class, event);
//                            myRealm.commitTransaction();
//                            // we are already in a separate thread here, so we can do some long operation
//
//                            //try {
//                            //  Thread.sleep(50);
//                            // } catch (InterruptedException e) {
//                            // }
//
//
//                        }
//
//
//                        // Log.d("TAG service", jsonResponse);
//                        //  Bundle getFinished = new Bundle();
//                        //  getFinished.putString("result", jsonResponse);
//                        //  resultReceiver.send(JSONSENT, getFinished);
//
//
//                   RealmResults<Event> results1 =
//                           myRealm.where(Event.class).findAll();
//
//                   for(Event c:results1) {
//                       Log.d("Realm EventLngLats: ", c.getVenue().getLocation().getLng_lat().toString());
//                       Log.d("Realm EventVenueName: ", c.getVenue().getName().toString());
//                       Log.d("Realm EventName: ", c.getName().toString());
//                       Log.d("Realm EventStartTime: ", c.getStartTime().toString());
//                       Log.d("Realm EventID: ", c.getId().toString());
//                   }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
            });
        */

            MyApiEndpointInterface service = MyApiEndpointInterface.retrofit.create(MyApiEndpointInterface.class);
            Call<JsonObject> call = service.getAllVenues(j);

            //Executing Call
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse (Call<JsonObject> call, final Response<JsonObject> response) {

                    try {

//                        Log.d("Response", response.toString());
                        Log.d("Response body", response.body().toString());
//                        Log.d("Response raw", response.raw().toString());
                        Log.d("Response pages", response.body().get("pages").toString());
                        pageCount = Integer.parseInt(response.body().get("pages").toString());
                        Log.d("PageCount", Integer.toString(pageCount));

                        Gson gson = new CustomGsonBuilder().create();
                        JsonArray json = response.body().getAsJsonArray("docs");
                        final List<Venue> objects = gson.fromJson(json, new TypeToken<List<Venue>>() {}.getType());
//                        Log.d("Response json", json.toString());


                        myRealm = Realm.getDefaultInstance();
                        myRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                myRealm.copyToRealmOrUpdate(objects);
                            }
                        });

                        Venue result =
                                myRealm.where(Venue.class).findFirst();
                        Log.d("Full Venue", result.toString());
                        myRealm.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Throw", t.toString());
                }
            });

        }
//        myRealm.commitTransaction();
//        myRealm.close();

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdate(String param1, String param2) {

    }

}
