package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.maps.ClusterMapInfoWindow;
import com.pk.tagger.maps.ClusterMapRender;
import com.pk.tagger.maps.ClusterMarkerLocation;
import com.pk.tagger.realm.event.Event;

import java.util.Date;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;


public class MapFragment extends Fragment implements GoogleMap.OnMapLongClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    MapView mMapView;
    private GoogleMap googleMap;
    private int zoomLevel = 10;
    SharedPreferences sharedPreferencesDate;
    private int mYear, mMonth, mDay, mHour, mMinute;

    FilterManager filterManager;

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
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

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
        final ClusterMapInfoWindow clusterMapInfoWindow = new ClusterMapInfoWindow();

        clusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(clusterMapInfoWindow);

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
                clusterMapInfoWindow.setData(cluster);
                clusterMapInfoWindow.setContext(getContext());
                return false;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarkerLocation>() {
            @Override
            public boolean onClusterItemClick(ClusterMarkerLocation clusterMarkerLocation) {
                Toast.makeText(getContext(), "1 Cluster Marker Clicked", Toast.LENGTH_SHORT).show();
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
                String ID = clusterMarkerLocation.getEventID();
                Intent intent = new Intent(getContext(), EventDetailActivity.class);
                intent.putExtra("EventID", ID);
                startActivity(intent);
            }
        });
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

