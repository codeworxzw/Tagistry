package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.pk.tagger.R;
import com.pk.tagger.maps.ClusterMapRender;
import com.pk.tagger.maps.ClusterMarkerLocation;
import com.pk.tagger.realm.TagData;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public class MapFragment extends Fragment implements GoogleMap.OnMapLongClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    MapView mMapView;
    private GoogleMap googleMap;
    private int zoomLevel = 10;
    SharedPreferences sharedPreferencesDate;
    //private static RealmConfiguration mRealmConfig;

    private Realm myRealm;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        //if (mRealmConfig == null) {
          //  mRealmConfig = new RealmConfiguration.Builder(getContext())
            //        .deleteRealmIfMigrationNeeded()
              //      .build();
        //}



        myRealm = Realm.getInstance(getActivity());

        //myRealm.setDefaultConfiguration(mRealmConfig);
        setUpMap();

        // Inflate the layout for this fragment
        return rootView;
    }

    public void setUpMap() {


        ClusterManager<ClusterMarkerLocation> clusterManager = new ClusterManager<ClusterMarkerLocation>( getContext(), googleMap );


        clusterManager.setRenderer(new ClusterMapRender(getContext(), googleMap, clusterManager));
        googleMap.setOnCameraChangeListener(clusterManager);
        LatLng kingston = new LatLng(51.413861, -0.299936);
      /*  googleMap.addMarker(new MarkerOptions()
                .position(kingston)
                .title("muppet")); */

      //  clusterManager.addItem(new ClusterMarkerLocation(kingston));

                LatLng streatham = new LatLng(51.419959, -0.128017);
      /*  googleMap.addMarker(new MarkerOptions()
                .position(streatham)
                .title("Super cool guy")
                .snippet("I'm so ronery")); */

        //clusterManager.addItem(new ClusterMarkerLocation(streatham));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(streatham, zoomLevel));

        //RealmConfiguration config = new RealmConfiguration.Builder(getContext())
          //      .deleteRealmIfMigrationNeeded()
            //    .build();

        sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", getActivity().MODE_PRIVATE);

        Date date = new Date(sharedPreferencesDate.getLong("Date", 0));

        RealmResults<TagData> results1 =
               myRealm.where(TagData.class)
                       .greaterThan("eventStartTime.local", date)
                       .findAll();

        try {
            for(TagData c:results1) {
                //  Log.d("results1", c.getLatLng());
                double latitude = c.getEventVenue().getEventVenue_Location().getLngLat().getLat();
                double longitude = c.getEventVenue().getEventVenue_Location().getLngLat().getLng();
                String title = c.getEventName();
                String venue = c.getEventVenue().getEventVenue_Name();
                //makeMarker(latitude, longitude, title, venue);
                LatLng adder = new LatLng( latitude, longitude );
                clusterManager.addItem( new ClusterMarkerLocation( adder, title, venue ));

            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }

        //googleMap.setOnMapLongClickListener(this);
        // Set a listener for info window events.

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarkerLocation>() {
            @Override
            public boolean onClusterItemClick(ClusterMarkerLocation clusterMarkerLocation) {
                Toast.makeText(getContext(), "Cluster Marker Clicked", Toast.LENGTH_SHORT).show();
                return false;
            }


        });

    }



    public void makeMarker(double latitude, double longitude,String name, String address) {

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
    public void onDestroyView() {
        myRealm.close();
        super.onDestroyView();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        //display in short period of time
        Toast.makeText(getActivity(), "Insert Event", Toast.LENGTH_SHORT).show();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(latLng.toString()));

//        Realm myRealm = Realm.getInstance(getContext());
//
//        myRealm.beginTransaction();
//
//        // Create an object
//        TagData Tag1 = myRealm.createObject(TagData.class);
//
//        // Set its fields
//        Tag1.setLatLng(latLng.toString());
//        Tag1.setLatitide(latLng.latitude);
//        Tag1.setLongitude(latLng.longitude);
//
//        myRealm.commitTransaction();

    }


}

