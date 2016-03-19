package com.pk.tagger.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pk.tagger.R;
import com.pk.tagger.realm.Event;
import com.pk.tagger.realm.EventsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by PK on 18/03/2016.
 */
public class ClusterInfoWindow extends AppCompatActivity {

    private static String TAG = ClusterInfoWindow.class.getSimpleName();
    Context context;

    private Date date;
    private EventsAdapter eventsRealmAdapter;
    private RealmRecyclerView realmRecyclerView;
    private Realm myRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cluster_info_window);
        ButterKnife.bind(this);
        context = getApplicationContext();
        ArrayList<String> EventIDs;
        EventIDs = getIntent().getStringArrayListExtra("ClusterIDs");

        String eventID = "1038341"; //default should return 'Katie Melua' event
        if (EventIDs != null) {
            Toast.makeText(this, EventIDs.toString(), Toast.LENGTH_SHORT).show();
        }

        realmRecyclerView = (RealmRecyclerView) findViewById(R.id.realmCluster_recycler_view);

        myRealm = Realm.getInstance(getApplicationContext());

        int eSize = EventIDs.size();
        Log.i("Size of arrayList: ", String.valueOf(eSize));

        // Build the query looking at all users:
        RealmQuery<Event> query = myRealm.where(Event.class).beginGroup();

        for (int i = 0; i < eSize; i++) {
            query.or().equalTo("eventID", EventIDs.get(i));
        }
        // Execute the query:
        RealmResults<Event> events = query.endGroup().findAll();

        /*
        RealmResults<Event> events = myRealm
                .where(Event.class)
                .equalTo("eventID", eventID)
                .findAll();  */

        getClusterList(events);

    }

    public void getClusterList(RealmResults<Event> events){

        int ticketMax = 1000;
        int ticketMin = 1;

        eventsRealmAdapter =
                new EventsAdapter(context, events, true, true, new EventsAdapter.OnItemClickListener() {
                    @Override public void onItemClick(Event item) {
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra("EventID", item.getEventID());
                        startActivity(intent);

                    }
                });
        realmRecyclerView.setAdapter(eventsRealmAdapter);
    }

}
