package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kyo.expandablelayout.ExpandableLayout;
import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.realm.MyRealmResults;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.event.EventsAdapter;
import com.pk.tagger.realm.venue.Venue;

import java.util.Date;
import java.util.Set;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;


public class ListingsFragment extends Fragment {

    private RealmRecyclerView realmRecyclerView;
    private EventsAdapter eventsRealmAdapter;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    private RealmResults<Event> mItems;
    private Date date;
    private Date endDate;
    FloatingActionButton fab;

    public ListingsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listings, container, false);
        Log.d("ListingsFragment", "onCreateView called");

        realmRecyclerView = (RealmRecyclerView) rootView.findViewById(R.id.realm_recycler_view);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                {
//                    ft.replace(R.id.container_body, new MapFragment(), "Map");
//                    // Set title bar
//                    ((MainActivity) getActivity())
//                            .setActionBarTitle("Map");
//                    ft.commit();
//                }
//
//            }
//        });
        fab.hide();

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
        MenuItem item=menu.findItem(R.id.action_mapview);
        item.setVisible(true);
        MenuItem item2=menu.findItem(R.id.action_listingsview);
        item2.setVisible(false);
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
        FilterManager filterManager = new FilterManager(getContext());
        if (filterManager.getDateStart()==0) {
            filterManager.setDefault();
        } else {
        }
        date = new Date(filterManager.getDateStart());
        endDate = new Date(filterManager.getDateEnd());

        String searchArtistVenue = filterManager.getSearchArtistVenue();
        Set<String> searchGenresTemp = filterManager.getSearchGenres();
        String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
        boolean ticketsAvailable = filterManager.getTicketsAvailable();

        int ticketMax = filterManager.getMaxPrice();;
        int ticketMin = 1;

        MyRealmResults myEvents = new MyRealmResults(getActivity(), searchArtistVenue, searchGenres, ticketsAvailable, ticketMin, ticketMax, date, endDate);
//        MyRealmResults myEvents = new MyRealmResults(getActivity(), "", searchGenres, false, 0, 1000, date, endDate);



        mItems = myEvents.getResults();
        long count = myEvents.getCount();
        Log.d("No. Events Found", String.valueOf(count));

        for (int i = 0; i < mItems.size(); i++) {
            expandState.append(i, false);
        }

        ((MainActivity) getActivity())
                .setActionBarTitle(String.valueOf(mItems.size()) + " Events");

        eventsRealmAdapter =
                new EventsAdapter(getContext(), mItems, true, true, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventsAdapter.ViewHolder holder = (EventsAdapter.ViewHolder) v.getTag();
                        holder.expandableLayout.toggleExpansion();

                        //temporarily save current expandstate for clicked item
                        boolean result = !expandState.get(holder.getAdapterPosition(), false);

                        //set all others to collapsed state i.e. only allow one expanded at a time, comment out to allow multiple expanded
                        for (int i = 0; i < mItems.size(); i++) {
                            expandState.append(i, false);
                        }

                        Log.d("ExpandResult", Boolean.toString(result));

                        //save current expandstate for clicked item
                        expandState.append(holder.getAdapterPosition(), result);
                    }
                }, new ExpandableLayout.OnExpandListener() {

                    private boolean isScrollingToBottom = false;

                    @Deprecated
                    @Override
                    public void onToggle(ExpandableLayout view, View child,
                                         boolean isExpanded) {
                    }

                    @Override
                    public void onExpandOffset(ExpandableLayout view, View child,
                                               float offset, boolean isExpanding) {
                        if (view.getTag() instanceof EventsAdapter.ViewHolder) {
                            final EventsAdapter.ViewHolder holder = (EventsAdapter.ViewHolder) view.getTag();
                            if (holder.getAdapterPosition() == mItems.size() - 1) {
                                if (!isScrollingToBottom) {
                                    isScrollingToBottom = true;
                                    realmRecyclerView.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isScrollingToBottom = false;
                                            realmRecyclerView.scrollToPosition(holder
                                                    .getAdapterPosition());
                                        }
                                    }, 100);
                                }
                            }
                        }
                    }
                }, expandState);
        realmRecyclerView.setAdapter(eventsRealmAdapter);
    }
}
