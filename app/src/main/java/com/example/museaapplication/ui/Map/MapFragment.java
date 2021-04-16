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

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MainActivity;
import com.example.museaapplication.ui.MuseuActivity;
import com.example.museaapplication.ui.home.HomeViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private MapViewModel mViewModel;
    private HomeViewModel mHomeViewModel;
    private MapView mMapView;
    private GoogleMap mMap;

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
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mHomeViewModel.getMuseums().observe(getViewLifecycleOwner(), new Observer<Museo[]>() {
            @Override
            public void onChanged(Museo[] museos) {
                museums = museos;
                for (Museo m : museos) {
                    /*Picasso.get().load(m.getImage()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            d = bitmap;
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });*/
                    if (m.getLocation() != null && m.getLocation().length != 0){
                        LatLng pos = new LatLng(m.getLocation()[0].getNumberDecimal(), m.getLocation()[1].getNumberDecimal());
                        mMap.addMarker(new MarkerOptions().position(pos).title(m.getName()).snippet(m.getDescriptions().getEn()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        );
                    }
                }
            }
        });
        initGoogleMap(savedInstanceState);
        /*TextView txt = super.getActivity().findViewById(R.id.title_test);
        txt.setText(R.string.title_maps);*/
        return root;
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            LatLng sydney = new LatLng(0, 0);
            MarkerOptions marker = new MarkerOptions()
                    .position(sydney)
                    .title("Marker in Sydney");
            map.addMarker(marker);
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    int position = getMPosition(marker.getTitle());
                    if (position >= 0) {
                        Intent i = new Intent(getContext(), MuseuActivity.class);
                        i.putExtra("Museu", (Serializable) museums[position]);
                        startActivity(i);
                    }
                    //Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        } else requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
}