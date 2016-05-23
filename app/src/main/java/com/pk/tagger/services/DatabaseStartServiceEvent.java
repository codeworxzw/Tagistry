package com.pk.tagger.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pk.tagger.activity.MyApiEndpointInterface;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.venue.Venue;
import com.pk.tagger.restclient.EventRestClient;
import com.pk.tagger.restclient.EventsRestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.exceptions.RealmException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DatabaseStartServiceEvent extends IntentService {
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

    public DatabaseStartServiceEvent() {
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
        Intent intent = new Intent(context, DatabaseStartServiceEvent.class);
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
        Intent intent = new Intent(context, DatabaseStartServiceEvent.class);
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

        final String eventID = param1;



//        final String test = "1005164";
//        EventRestClient.get("/" +eventID, null, new JsonHttpResponseHandler() {
//
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
////                Log.d("JSONObject", "JSONObject received");
//                final JSONObject event = response;
//
//                try {
//                    myRealm.getDefaultInstance();
//                    myRealm.executeTransaction(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm realm) {
//                            myRealm.createOrUpdateObjectFromJson(Event.class, event);
//                        }
//                    });
//
//                    Event result =
//                            myRealm.where(Event.class).contains("id", eventID).findFirst();
//                    myRealm.close();

//                    Log.d("Full Event", result.toString());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        MyApiEndpointInterface service = MyApiEndpointInterface.retrofit.create(MyApiEndpointInterface.class);

        Call<Event> call = service.getEvent(eventID);

        //Executing Call
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse (Call<Event> call, final Response<Event> response) {

                try {
                    Log.d("Response body1", response.body().toString());

                    myRealm = Realm.getDefaultInstance();

                    myRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            myRealm.copyToRealmOrUpdate(response.body());
                        }
                    });
                    Event result =
                            myRealm.where(Event.class).equalTo("id", eventID).findFirst();
                    myRealm.close();

                    Log.d("Full Event", result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.d("Throw", t.toString());
            }
        });


    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdate(String param1, String param2) {

    }

}
