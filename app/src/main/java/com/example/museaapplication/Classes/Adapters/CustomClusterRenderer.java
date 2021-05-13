package com.example.museaapplication.Classes.Adapters;

import android.content.Context;
import android.graphics.Color;

import com.example.museaapplication.Classes.MyClusterItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<MyClusterItem> {

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<MyClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected int getColor(int clusterSize) {
        if (clusterSize <= 10) return Color.argb(255,56, 232, 173);
        return Color.BLUE; // Return any color you want here. You can base it on clusterSize.
    }

    @Override
    protected void onClusterItemRendered(MyClusterItem clusterItem, Marker marker) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        super.onClusterItemRendered(clusterItem, marker);
    }
}
