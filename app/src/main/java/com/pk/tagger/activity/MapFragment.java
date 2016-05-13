package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.maps.ClusterMapInfoWindow;
import com.pk.tagger.maps.ClusterMapRender;
import com.pk.tagger.maps.ClusterMarkerLocation;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.event.EventsAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class MapFragment extends Fragment implements GoogleMap.OnMapLongClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    MapView mMapView;
    private GoogleMap googleMap;
    FloatingActionButton fab;
    String DEBUG_TAG = "Map Debugger";
    private int zoomLevel = 10;
    SharedPreferences sharedPreferencesDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Rect rectf;
    static final float IMAGE_SIZE_RATIO = .15f; //define how big your marker is relative to the total screen

    FilterManager filterManager;

    private EventsAdapter eventsRealmAdapter;
    private RealmRecyclerView realmRecyclerView;
    private Realm myRealm;
    private int height, top, bottom, width;
    public MapFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        mMapView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mMapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        rectf = new Rect();
                        mMapView.getGlobalVisibleRect(rectf);

                        Log.d("WIDTH        :",String.valueOf(rectf.width()));
                        height = rectf.height();
                        width = rectf.width();
                        top = rectf.top;
                        bottom = rectf.bottom;
                        Log.d("HEIGHT       :",String.valueOf(rectf.height()));
                        Log.d("left         :",String.valueOf(rectf.left));
                        Log.d("right        :",String.valueOf(rectf.right));
                        Log.d("top          :",String.valueOf(rectf.top));
                        Log.d("bottom       :",String.valueOf(rectf.bottom));
                    }

                }
        );

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                {
                    ft.replace(R.id.container_body, new ListingsFragment(), "Event Listings");
                    // Set title bar
                    ((MainActivity) getActivity())
                            .setActionBarTitle("Event Listings");
                    ft.commit();
                }

            }
        });

        View myView = rootView.findViewById(R.id.borderview);

        myView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // ... Respond to touch events
                //Log.d(DEBUG_TAG,event.toString());

                resize(event.getRawY());
                int action = MotionEventCompat.getActionMasked(event);
                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :
                        //Log.d(DEBUG_TAG,"Action was DOWN");
                        return true;
                    case (MotionEvent.ACTION_MOVE) :
                        Log.d(DEBUG_TAG,"Action was MOVE");
                        Log.d("X", String.valueOf(event.getX()));
                        Log.d("Y", String.valueOf(event.getRawY()));
                        //Down Y means positive Up Y means negative

                        return true;
                    case (MotionEvent.ACTION_UP) :
                        Log.d(DEBUG_TAG,"Action was UP");
                        return true;
                    case (MotionEvent.ACTION_CANCEL) :
                        Log.d(DEBUG_TAG,"Action was CANCEL");
                        return true;
                    case (MotionEvent.ACTION_OUTSIDE) :
                        Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
                                "of current screen element");
                        return true;
                    default :

                }
                return true;
            }
        });



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        setUpMap();
        // Inflate the layout for this fragment
        return rootView;
    }

    private void resize(float y) {

        float resized = 1 - ((y - top) / (height / 2)) / 2; // 1 : 1 split 1864:708 3:1

        float resized2 = (y - top) / (height / 2) / 2; // 1 : 1
        // Log.d("resized", String.valueOf(resized));
        // Log.d("resized2", String.valueOf(resized2));
        if (resized2 < 0.8) {
            LinearLayout lnr = (LinearLayout) getActivity().findViewById(R.id.llview1);
            LinearLayout.LayoutParams lnrp = (LinearLayout.LayoutParams) getActivity().findViewById(R.id.llview1).getLayoutParams();
            lnrp.weight = resized;
            lnr.setLayoutParams(lnrp);
            LinearLayout lnr2 = (LinearLayout) getActivity().findViewById(R.id.llview2);
            LinearLayout.LayoutParams lnrp2 = (LinearLayout.LayoutParams) getActivity().findViewById(R.id.llview2).getLayoutParams();
            lnrp2.weight = resized2;
            lnr2.setLayoutParams(lnrp2);
        } else {
            LinearLayout lnr = (LinearLayout) getActivity().findViewById(R.id.llview1);
        LinearLayout.LayoutParams lnrp = (LinearLayout.LayoutParams) getActivity().findViewById(R.id.llview1).getLayoutParams();
        lnrp.weight = 0f;
        lnr.setLayoutParams(lnrp);

    }

    }

    public void setUpMap() {

        FilterManager filterManager = new FilterManager(getContext());
        if (filterManager.getDateStart()==0) {
            filterManager.setDefault();
        } else {
        }

        final ClusterManager<ClusterMarkerLocation> clusterManager = new ClusterManager<ClusterMarkerLocation>( getContext(), googleMap );
        clusterManager.setRenderer(new ClusterMapRender(getContext(), googleMap, clusterManager));
        googleMap.setOnCameraChangeListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setOnInfoWindowClickListener(clusterManager);
        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
       // final ClusterMapInfoWindow clusterMapInfoWindow = new ClusterMapInfoWindow();

       // clusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(clusterMapInfoWindow);

        LatLng streatham = new LatLng(51.419959, -0.128017);
//        googleMap.addMarker(new MarkerOptions()
//                .position(streatham)
//                .title("Super cool guy")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.note_listings)));       //need to resize image first

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(streatham, zoomLevel));

//        sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", getActivity().MODE_PRIVATE);
//        Date date = new Date(sharedPreferencesDate.getLong("Date", 0));
//        Date endDate = new Date(sharedPreferencesDate.getLong("DateEnd", 0));
//        SharedPreferences prefs = getContext().getSharedPreferences("FILTER_FILE_KEY", Context.MODE_PRIVATE);
//        String searchArtistVenue = prefs.getString("search_artist_venue", "");
//        Set<String> searchGenresTemp = prefs.getStringSet("search_genres", null);
//        String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
//        boolean ticketsAvailable = prefs.getBoolean("tickets_available", false);


        Date date = new Date(filterManager.getDateStart());
        Date endDate = new Date(filterManager.getDateEnd());

        String searchArtistVenue = filterManager.getSearchArtistVenue();
        Set<String> searchGenresTemp = filterManager.getSearchGenres();
        String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
        boolean ticketsAvailable = filterManager.getTicketsAvailable();

        int ticketMax = filterManager.getMaxPrice();
        int ticketMin = 1;

        MyRealmResults events2 = new MyRealmResults(getActivity(), searchArtistVenue, searchGenres, ticketsAvailable, ticketMin, ticketMax, date, endDate);
        RealmResults <Event> events = events2.getResults();

        try {
            for(Event c:events) {
                //  Log.d("results1", c.getLatLng());
                double latitude = c.getVenue().getLocation().getLng_lat().getLat();
                double longitude = c.getVenue().getLocation().getLng_lat().getLng();
                String title = c.getArtist().getName();
                String venue = c.getVenue().getName();
                String eventId = c.getId();
                //makeMarker(latitude, longitude, title, venue);
                LatLng adder = new LatLng( latitude, longitude );
                clusterManager.addItem( new ClusterMarkerLocation( adder, title, venue, eventId ));

            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ClusterMarkerLocation>() {
            @Override
            public boolean onClusterClick(Cluster<ClusterMarkerLocation> cluster) {
                Toast.makeText(getContext(), "Multiple Cluster Marker Clicked", Toast.LENGTH_SHORT).show();
                LinearLayout lnr = (LinearLayout) getActivity().findViewById(R.id.llview1);
                LinearLayout.LayoutParams lnrp = (LinearLayout.LayoutParams) getActivity().findViewById(R.id.llview1).getLayoutParams();
                lnrp.weight = 1f;
                lnr.setLayoutParams(lnrp);

                Collection<ClusterMarkerLocation> items = cluster.getItems();

                ArrayList<String> bulk = new ArrayList<String>();
                if (cluster != null) {
                    for (ClusterMarkerLocation item : items) {
                        bulk.add(item.getEventID());
                        Log.i("MyMaps", "ID: " + item.getEventID());
                    }
                }
                realmRecyclerView = (RealmRecyclerView) getActivity().findViewById(R.id.realmCluster_recycler_view2);

                myRealm = Realm.getDefaultInstance();

                int eSize = bulk.size();
                Log.i("Size of arrayList: ", String.valueOf(eSize));

                // Build the query looking at all users:
                RealmQuery<Event> query = myRealm.where(Event.class).beginGroup();

                for (int i = 0; i < eSize; i++) {
                    query.or().equalTo("id", bulk.get(i));
                }
                // Execute the query:
                RealmResults<Event> events = query.endGroup().findAll();

                getClusterList(events);

        /*
        RealmResults<Event> events = myRealm
                .where(Event.class)
                .equalTo("eventID", eventID)
                .findAll();  */

                myRealm.close();


                //clusterMapInfoWindow.setData(cluster);
                //clusterMapInfoWindow.setContext(getContext());
                return false;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarkerLocation>() {
            @Override
            public boolean onClusterItemClick(ClusterMarkerLocation clusterMarkerLocation) {
                Toast.makeText(getContext(), "1 Cluster Marker Clicked", Toast.LENGTH_SHORT).show();
                Log.d("1 cluster item", clusterMarkerLocation.getEventID());
                //ArrayList<String> bulk = new ArrayList<String>();
                //bulk.add(clusterMarkerLocation.getEventID());

//                LatLngBounds b = googleMap.getProjection().getVisibleRegion().latLngBounds;
//                Float distance = distanceBetweenPoints(b.northeast, b.southwest) * IMAGE_SIZE_RATIO;
//                for (Marker m : markers ) {
//                    if (marker.equals(m) ) { continue; } //don't compare the same one
//                    if (distanceBetweenPoint(m.getPosition(), marker.getPosition()) {
//                /*Note do onMarkerClick this as an Aynch task and continue along
//                if also want to fire off against the main object.
//                */
//                      //  return onMarkerClick(m);
//                    }
//                }

                LinearLayout lnr = (LinearLayout) getActivity().findViewById(R.id.llview1);
                LinearLayout.LayoutParams lnrp = (LinearLayout.LayoutParams) getActivity().findViewById(R.id.llview1).getLayoutParams();
                lnrp.weight = 1f;
                lnr.setLayoutParams(lnrp);
                realmRecyclerView = (RealmRecyclerView) getActivity().findViewById(R.id.realmCluster_recycler_view2);

                myRealm = Realm.getDefaultInstance();

                RealmResults<Event> events = myRealm.where(Event.class)
                        .equalTo("id", clusterMarkerLocation.getEventID())
                        .findAll();

                getClusterList(events);

                myRealm.close();
                return false;
            }
        });

        clusterManager.setOnClusterInfoWindowClickListener(new ClusterManager.OnClusterInfoWindowClickListener<ClusterMarkerLocation>() {

            @Override
            public void onClusterInfoWindowClick(Cluster<ClusterMarkerLocation> cluster) {
                Toast.makeText(getContext(), "Cluster Window Clicked", Toast.LENGTH_SHORT).show();

            }

        });

        clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<ClusterMarkerLocation>() {

            @Override
            public void onClusterItemInfoWindowClick(ClusterMarkerLocation clusterMarkerLocation) {
                Toast.makeText(getContext(), "1 Cluster Window Clicked", Toast.LENGTH_SHORT).show();
              //  String ID = clusterMarkerLocation.getEventID();
               // Intent intent = new Intent(getContext(), EventDetailActivity.class);
               // intent.putExtra("EventID", ID);
               // startActivity(intent);
            }
        });
        }

    public void getClusterList(RealmResults<Event> events){
        //Log.d("events: ", events.toString());
        int ticketMax = 1000;
        int ticketMin = 1;

        eventsRealmAdapter =
                new EventsAdapter(getContext(), events, true, true, new EventsAdapter.OnItemClickListener() {
                    @Override public void onItemClick(Event item) {
                        Intent intent = new Intent(getContext(), EventDetailActivity.class);
                        intent.putExtra("EventID", item.getId());
                        startActivity(intent);

                    }
                });
        realmRecyclerView.setAdapter(eventsRealmAdapter);
    }

    public void makeMarker(double latitude, double longitude, String name, String address) {

        LatLng latlng = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title(name)
                .snippet(address));
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
    public void onResume() {
        super.onResume();
        //setUpMap();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        //display in short period of time
        Toast.makeText(getActivity(), "Insert Event", Toast.LENGTH_SHORT).show();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(latLng.toString()));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item= menu.findItem(R.id.action_sync);
        item.setVisible(false);
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

}

