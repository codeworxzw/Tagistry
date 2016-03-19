package com.pk.tagger.maps;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.pk.tagger.R;
import com.pk.tagger.activity.ClusterInfoWindow;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.Inflater;

/**
 * Created by PK on 18/03/2016.
 */
public class ClusterMapInfoWindow implements GoogleMap.InfoWindowAdapter {

    Cluster<ClusterMarkerLocation> clickedCluster = null;

    public void setContext(Context context) {
        this.context = context;
    }

    Context context;

    public void setData(Cluster<ClusterMarkerLocation> clickedCluster) {
        this.clickedCluster = clickedCluster;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Log.d("Hello 1", marker.toString());
        Collection<ClusterMarkerLocation> items = clickedCluster.getItems();
        TextView clusterText = new TextView(context);
        ArrayList<String> bulk = new ArrayList<String>();
        if (clickedCluster != null) {
            for(ClusterMarkerLocation item : items) {
                bulk.add(item.getEventID());
                Log.i("MyMaps","ID: " + item.getEventID());
            }
            clusterText.setText(bulk.toString());
            Intent intent = new Intent(context, ClusterInfoWindow.class);
            intent.putStringArrayListExtra("ClusterIDs", bulk);
            context.startActivity(intent);
        }

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.d("Hello 2", marker.toString());
        Collection<ClusterMarkerLocation> items = clickedCluster.getItems();
        if (clickedCluster != null) {
            for(ClusterMarkerLocation item : items) {
                Log.i("MyMaps","ID: " + item.getEventID());
            }
        }
            return null;
        }

}