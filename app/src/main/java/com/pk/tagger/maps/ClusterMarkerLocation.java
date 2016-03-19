package com.pk.tagger.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by PK on 27/02/2016.
 */
public class ClusterMarkerLocation implements ClusterItem{

    private LatLng position;

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    String eventID;
    String title;
    String snippet;

    public String getEventID() {
        return eventID;
    }

    public ClusterMarkerLocation( LatLng latLng, String tit ,String sni, String eventID ) {
        position = latLng;
        title = tit;
        snippet = sni;
        this.eventID = eventID;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition( LatLng position ) {
        this.position = position;
    }


}
