package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.event.EventsAdapter;
import com.pk.tagger.recyclerview.MyScrollListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class ListingsFragment extends Fragment {

    private Realm myRealm;
    private RealmRecyclerView realmRecyclerView;
    private EventsAdapter eventsRealmAdapter;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    private RealmResults<Event> mItems;
    private Date date;
    private Date endDate;
    FloatingActionButton fab;
    FilterManager filterManager;

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

        myRealm = Realm.getDefaultInstance();

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



        getListings(false);

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

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        filterManager = new FilterManager(getContext());

        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
       // searchView.setQueryHint(filterManager.getSearchArtistVenue());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Onquery", query);

                filterManager.setSearchArtistVenue(query);
                getListings(true);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("OnChange", newText);

                filterManager.setSearchArtistVenue(newText);
                getListings(true);

                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getListings(false);
                return true;
            }
        });

        MenuItem item3=menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(item3, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getListings(false);
                return true;
            }
        });


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

    public void getListings(boolean textSearch){


        if (textSearch) {

            FilterManager filterManager = new FilterManager(getContext());
            if (filterManager.getDateStart() == 0) {
                filterManager.setDefault();
            } else {
            }

            Calendar calendarDate = new GregorianCalendar();
            date = new Date(calendarDate.getTimeInMillis());
            calendarDate.add(Calendar.MONTH, 6);
            endDate = new Date(calendarDate.getTimeInMillis());
            String searchArtistVenue = filterManager.getSearchArtistVenue();
            boolean ticketsAvailable = false;
            int ticketMax = 0;
            int ticketMin = 1;
            Set<String> Genres = new HashSet<String>(Arrays.asList(getContext().getResources().getStringArray(R.array.genres_values)));
            String[] searchGenres = Genres.toArray(new String[Genres.size()]);
            MyRealmResults myEvents = new MyRealmResults(getActivity(), myRealm, searchArtistVenue, searchGenres, ticketsAvailable, ticketMin, ticketMax, date, endDate);
            mItems = myEvents.getResults();

        } else {

            FilterManager filterManager = new FilterManager(getContext());
            if (filterManager.getDateStart() == 0) {
                filterManager.setDefault();
            } else {
            }
            date = new Date(filterManager.getDateStart());
            endDate = new Date(filterManager.getDateEnd());

            String searchArtistVenue = filterManager.getSearchArtistVenue();
            Set<String> searchGenresTemp = filterManager.getSearchGenres();
            String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
            boolean ticketsAvailable = filterManager.getTicketsAvailable();

            int ticketMax = filterManager.getMaxPrice();

            int ticketMin = 1;

            MyRealmResults myEvents = new MyRealmResults(getActivity(), myRealm, searchArtistVenue, searchGenres, ticketsAvailable, ticketMin, ticketMax, date, endDate);
            mItems = myEvents.getResults();
        }

//        MyRealmResults myEvents = new MyRealmResults(getActivity(), "", searchGenres, false, 0, 1000, date, endDate);

        Log.d("No. Events Found", String.valueOf(mItems.size()));

        for (int i = 0; i < mItems.size(); i++) {
            expandState.append(i, false);
        }

        ((MainActivity) getActivity())
                .setActionBarTitle(String.valueOf(mItems.size()) + " Events");

        eventsRealmAdapter = new EventsAdapter(getContext(), mItems, true, true, new View.OnClickListener() {
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
                }, expandState, myRealm);
        realmRecyclerView.setAdapter(eventsRealmAdapter);
        eventsRealmAdapter.updateRealmResults(mItems);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);



    }

    public void searchListings() {


    }


}
