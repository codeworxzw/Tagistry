package com.pk.tagger.MapsTemp;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by PK on 27/02/2016.
 */
public class ClusterMapRender extends DefaultClusterRenderer <ClusterMarkerLocation> {

    public ClusterMapRender(Context context, GoogleMap map,
                       ClusterManager<ClusterMarkerLocation> clusterManager) {
        super(context, map, clusterManager);
    }

    protected void onBeforeClusterItemRendered(ClusterMarkerLocation item, MarkerOptions markerOptions) {

        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
