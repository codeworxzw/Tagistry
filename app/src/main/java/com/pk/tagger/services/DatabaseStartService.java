package com.pk.tagger.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pk.tagger.restclient.EventsRestClient;
import com.pk.tagger.realm.event.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DatabaseStartService extends IntentService {
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

    public DatabaseStartService() {
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
        Intent intent = new Intent(context, DatabaseStartService.class);
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
        Intent intent = new Intent(context, DatabaseStartService.class);
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

        SharedPreferences sharedPreferences = getSharedPreferences("TimeStamp", Context.MODE_PRIVATE);
        Date date = new Date(System.currentTimeMillis());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("time", date.getTime());
        editor.commit();

        Date myDate = new Date(sharedPreferences.getLong("time", 0));

        Log.d("Date in DStartService", myDate.toString());
        myRealm = Realm.getDefaultInstance();

        myRealm.beginTransaction();

        EventsRestClient.get("", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("JSONObject", "JSONObject received");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {

                Log.d("JSONArray", "JSONArray received");


                try {
                    // Parsing json array response
                    // loop through each json object
                    //jsonResponse = "";
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject event = (JSONObject) data
                                .get(i);

                      //  jsonResponse = event.toString();

                        //Log.d("jsonResponse", jsonResponse);
                        myRealm.createObjectFromJson(Event.class, event);
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

        myRealm.commitTransaction();
        Log.d("Service", "Finished");
        myRealm.close();

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdate(String param1, String param2) {

    }

}
