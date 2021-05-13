package com.example.museaapplication.ui.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museaapplication.Classes.Adapters.CustomClusterRenderer;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.MyClusterItem;
import com.example.museaapplication.Classes.Permissions;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MainActivity;
import com.example.museaapplication.ui.MuseuActivity;
import com.example.museaapplication.ui.home.HomeViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, Permissions {

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private MapViewModel mViewModel;
    private HomeViewModel mHomeViewModel;
    private MapView mMapView;
    private GoogleMap mMap;

    private String id;

    ClusterManager<MyClusterItem> manager;

    public static MapFragment newInstance() {
        return new MapFragment();
    }



    Bitmap d = null;

    Museo[] museums;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.map_fragment, container, false);
        mMapView = root.findViewById(R.id.map_view);
        mHomeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        mHomeViewModel.getMuseums().observe(getViewLifecycleOwner(), new Observer<Museo[]>() {
            @Override
            public void onChanged(Museo[] museos) {
                museums = museos;
                for (Museo m : museos) {
                    if (m.getLocation() != null && m.getLocation().length != 0){
                        LatLng pos = new LatLng(m.getLocation()[0].getNumberDecimal(), m.getLocation()[1].getNumberDecimal());
                        MyClusterItem item = new MyClusterItem(m.getLocation()[0].getNumberDecimal(), m.getLocation()[1].getNumberDecimal(), m.getName(), m.getAddress());
                        item.setId(m.get_id());
                        //mMap.addMarker(new MarkerOptions().position(pos).title(m.getName()).snippet(m.get_id()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        manager.addItem(item);
                    }
                }
            }
        });
        initGoogleMap(savedInstanceState);
        /*TextView txt = super.getActivity().findViewById(R.id.title_test);
        txt.setText(R.string.title_maps);*/
        return root;
    }

    private void addItems() {
        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyClusterItem offsetItem = new MyClusterItem(lat, lng, "Title " + i, "Snippet " + i);
            manager.addItem(offsetItem);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        // TODO: Use the ViewModel
        //super.getActivity().getActionBar();
       /* ActionBar actionBar = super.getActivity().getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.appbar_layout_test);*/
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        manager = new ClusterManager<>(getActivity(), mMap);
        mMap.setOnCameraIdleListener(manager);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            /*map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    int position = getMPosition(marker.getTitle());
                    if (position >= 0) {
                        Intent i = new Intent(getContext(), MuseuActivity.class);
                        Uri uri = Uri.parse("/museums/" + id);
                        i.setData(uri);
                        i.putExtra("Museu", (Serializable) museums[position]);
                        startActivity(i);
                    }
                }
            });*/
            map.setOnMarkerClickListener(manager);
            // When clicking a cluster make it zoom in


            manager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyClusterItem>() {
                        @Override
                        public boolean onClusterClick(final Cluster<MyClusterItem> cluster) {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    cluster.getPosition(), (float) Math.floor(map
                                            .getCameraPosition().zoom + 1)), 300,
                                    null);
                            return true;
                        }
                    });
            //
            manager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyClusterItem>() {
                @Override
                public boolean onClusterItemClick(MyClusterItem item) {
                    id = item.getId();
                    return false;
                }
            });
            manager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MyClusterItem>() {
                @Override
                public void onClusterItemInfoWindowClick(MyClusterItem item) {
                    // Aqui puedo poner la distincion de si el museo ya esta descargado o hay que descargarlo para la vista.
                    // Si la funcion encuentra el museo entonces este ya esta en el sistema y le pasamos este, en caso contrario pasamos la uri
                    // y descargamos ese museo de la API.
                    int position = getMPosition(item.getTitle());
                    Intent i = new Intent(getContext(), MuseuActivity.class);
                    if (position >= 0) {
                        MuseuActivity.curMuseum = museums[position];
                        startActivityForResult(i, 1 );
                    }else {
                        Uri uri = Uri.parse("/museums/" + id);
                        i.setData(uri);
                    }
                }
            });
            manager.setRenderer(new CustomClusterRenderer(getActivity(), map, manager));

        }
    }

    private int getMPosition(String title){
        int i = 0;
        for(Museo m : museums){
            if (m.getName().equals(title)) return i;
            i++;
        }
        return -1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady(mMap);
                }else Toast.makeText(getContext(), R.string.perm_denegado, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void Granted() {
        onMapReady(mMap);
    }
}