package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pk.tagger.R;
import com.pk.tagger.realm.Event;
import com.pk.tagger.realm.EventsAdapter;

import java.util.Date;
import java.util.Set;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmResults;


public class ListingsFragment extends Fragment {

    private Date date;
    private EventsAdapter eventsRealmAdapter;
    private RealmRecyclerView realmRecyclerView;

    SharedPreferences sharedPreferencesDate;
    private int mYear, mMonth, mDay, mHour, mMinute;

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
        Log.d("ListingsFragment", "onCreateView called");

        realmRecyclerView = (RealmRecyclerView) rootView.findViewById(R.id.realm_recycler_view);

        getListings();

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
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("ListingsFragment", "onAttach called");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("ListingsFragment", "onDetach called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("ListingsFragment", "onPause called");

    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("ListingsFragment", "onStop called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ListingsFragment", "onResume called");
        //eventsRealmAdapter.notifyDataSetChanged();        //doesnt do anything...
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        //MenuItem item=menu.findItem(R.id.action_sync);
        //item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            default:
                return false;
                // /return super.onOptionsItemSelected(item);
        }
    }

    public void getListings(){
        sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", getActivity().MODE_PRIVATE);
        date = new Date(sharedPreferencesDate.getLong("Date", 0));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String searchArtistVenue = prefs.getString("search_artist_venue", "");
        Set<String> searchGenresTemp = prefs.getStringSet("search_genres", null);
        String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
        boolean ticketsAvailable = prefs.getBoolean("tickets_available", false);

        int ticketMax = 1000;
        int ticketMin = 1;

        MyRealmResults events2 = new MyRealmResults(getActivity(), searchArtistVenue, searchGenres, ticketsAvailable, ticketMin, ticketMax, date);
        RealmResults events = events2.getResults();
        long count = events2.getCount();
        Log.d("No. Events Found", String.valueOf(count));

        eventsRealmAdapter =
                new EventsAdapter(getContext(), events, true, true, new EventsAdapter.OnItemClickListener() {
                    @Override public void onItemClick(Event item) {
                        Intent intent = new Intent(getContext(), EventDetailActivity.class);
                        intent.putExtra("EventID", item.getEventID());
                        startActivity(intent);

                    }
                });
        realmRecyclerView.setAdapter(eventsRealmAdapter);
    }
}
