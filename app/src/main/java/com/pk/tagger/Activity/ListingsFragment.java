package com.pk.tagger.Activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pk.tagger.R;
import com.pk.tagger.Realm.Event;
import com.pk.tagger.Realm.TagDataAdapter;

//import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import java.util.Date;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;


public class ListingsFragment extends Fragment {

    private Realm myRealm;

    SharedPreferences sharedPreferencesDate;

    public ListingsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
       // resetRealm();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listings, container, false);
        Log.d("Realm Tag onCV", "open");

        sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", getActivity().MODE_PRIVATE);

        Date date = new Date(sharedPreferencesDate.getLong("Date", 0));

        myRealm = Realm.getInstance(getContext());
        RealmResults<Event> events = myRealm
                .where(Event.class)
                .greaterThan("eventStartTime.local", date)
                .findAll();
        TagDataAdapter toDoRealmAdapter =
                new TagDataAdapter(getContext(), events, true, true, new TagDataAdapter.OnItemClickListener() {
                    @Override public void onItemClick(Event item) {
                        //Log.d("Popup event",item.getEventTickets().toString());
                        //Toast.makeText(getContext(), "Item: " + item.toString(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), EventDetailActivity.class);
                        intent.putExtra("EventID", item.getEventID());
                        startActivity(intent);
                    }
                });
        RealmRecyclerView realmRecyclerView =
                (RealmRecyclerView) rootView.findViewById(R.id.realm_recycler_view);
        realmRecyclerView.setAdapter(toDoRealmAdapter);

        Log.d("date", date.toString());

        return rootView;
    }

    @Override
    public void onStart(){

        super.onStart();

    }

    @Override
    public void onDestroyView(){
        Log.d("Realm Tag onDV", "open");

        super.onDestroyView();
        myRealm.close();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item=menu.findItem(R.id.action_sync);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_clear_cache:
                myRealm.beginTransaction();
                myRealm.clear(Event.class);
                myRealm.commitTransaction();

                return true;

            case R.id.action_sync:

                return false;

            default:
                return false;
                // /return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onStop() {
        super.onStop();

    }
}
