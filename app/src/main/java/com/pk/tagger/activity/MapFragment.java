package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
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
import com.pk.tagger.R;
import com.pk.tagger.realm.TagData;

import io.realm.Realm;
import io.realm.RealmResults;


public class MapFragment extends Fragment implements GoogleMap.OnMapLongClickListener {

    MapView mMapView;
    private GoogleMap googleMap;
    private int zoomLevel = 10;
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



        myRealm = Realm.getInstance(getContext());

        //myRealm.setDefaultConfiguration(mRealmConfig);
        setUpMap();

        // Inflate the layout for this fragment
        return rootView;
    }

    public void setUpMap() {


        LatLng kingston = new LatLng(51.413861, -0.299936);
        googleMap.addMarker(new MarkerOptions()
                .position(kingston)
                .title("muppet"));

        LatLng streatham = new LatLng(51.419959, -0.128017);
        googleMap.addMarker(new MarkerOptions()
                .position(streatham)
                .title("Super cool guy")
                .snippet("I'm so ronery"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(streatham, zoomLevel));

        //RealmConfiguration config = new RealmConfiguration.Builder(getContext())
          //      .deleteRealmIfMigrationNeeded()
            //    .build();

       // Realm myRealm = Realm.getInstance();


        RealmResults<TagData> results1 =
               myRealm.where(TagData.class).findAll();

        for(TagData c:results1) {
          //  Log.d("results1", c.getLatLng());
            double latitude = c.getEventLatitude();
           double longitude = c.getEventLongitude();
            makeMarker(latitude, longitude);
        }

        //googleMap.setOnMapLongClickListener(this);
        // Set a listener for info window events.

    }

    public void makeMarker(double latitude, double longitude) {

        LatLng latlng = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title(latlng.toString()));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        myRealm.close();
        super.onDetach();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        //display in short period of time
        Toast.makeText(getActivity(), "Insert Event", Toast.LENGTH_SHORT).show();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(latLng.toString()));

        //Realm myRealm = Realm.getInstance(getContext());

      //  myRealm.beginTransaction();

        // Create an object
     //   TagData Tag1 = myRealm.createObject(TagData.class);

        // Set its fields
//        Tag1.setLatLng(latLng.toString());
  //      Tag1.setLatitide(latLng.latitude);
    //    Tag1.setLongitude(latLng.longitude);

      //  myRealm.commitTransaction();

    }


}

