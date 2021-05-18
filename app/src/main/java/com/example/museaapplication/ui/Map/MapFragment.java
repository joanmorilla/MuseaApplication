package com.example.museaapplication.ui.Map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.Classes.Adapters.CustomClusterRenderer;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.MyClusterItem;
import com.example.museaapplication.Classes.Permissions;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MuseuActivity;
import com.example.museaapplication.ui.home.HomeViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteFragment;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, Permissions {

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private MapViewModel mViewModel;
    private SearchView searchView;
    private HomeViewModel mHomeViewModel;
    private TextView myLocB;
    private MapView mMapView;
    private GoogleMap mMap;
    private LinearLayout mapsSuggestions;
    private SimpleCursorAdapter mAdapter;
    private List<Address> listAddressSugg;
    private String[] Suggestions;

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
        myLocB = root.findViewById(R.id.button_holder);
        searchView = root.findViewById(R.id.search_view_maps);
        mapsSuggestions = root.findViewById(R.id.maps_recomendations);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> listAddress = null;
                if (location != null || !location.equals("")){
                    Geocoder geocoderv2 = new Geocoder(requireContext());
                    try {
                        listAddress = geocoderv2.getFromLocationName(location, 1);
                        if (listAddress != null && listAddress.size() != 0) {
                            Address address = listAddress.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }else {
                            Toast.makeText(getContext(), "Could not find, try a different spelling", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onQueryTextChange(String s) {
                /*String location = searchView.getQuery().toString();
                listAddressSugg = null;
                if (location != null || !location.equals("")){
                    Geocoder geocoderv2 = new Geocoder(requireContext());
                    try {
                        listAddressSugg = geocoderv2.getFromLocationName(location, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (listAddressSugg != null) {
                        mapsSuggestions.removeAllViews();
                        for (Address a : listAddressSugg){
                            TextView suggestion = new TextView(requireContext());
                            suggestion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            suggestion.setText(a.getFeatureName());
                            mapsSuggestions.addView(suggestion);
                        }
                    }
                }*/
                return false;
            }
        });

        mHomeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
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
        manager = new ClusterManager<>(requireActivity(), mMap);
        mMap.setOnCameraIdleListener(manager);
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                Uri gmmIntentUri = Uri.parse("geo:" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "?q=" + marker.getTitle() + marker.getSnippet());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                Log.e("Aver", marker.getSnippet());
            }
        });

        if (isDarkMode())
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.maps_dark_style));

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            View compassButton = mMapView.findViewWithTag("GoogleMapCompass");
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
            // position on top right
            rlp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_START,0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            rlp.bottomMargin = pixToDp(150);
            rlp.rightMargin = pixToDp(25);
            compassButton.setLayoutParams(rlp);
            map.setOnMarkerClickListener(manager);
            locationButton.setVisibility(View.GONE);


            requireActivity().findViewById(R.id.button_holder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationButton.callOnClick();
                }
            });
            // When clicking a cluster make it zoom in
            mHomeViewModel.getMuseums().observe(getViewLifecycleOwner(), new Observer<Museo[]>() {
                @Override
                public void onChanged(Museo[] museos) {
                    museums = museos;
                    for (Museo m : museos) {
                        if (m.getLocation() != null && m.getLocation().length != 0){
                            MyClusterItem item = new MyClusterItem(m.getLocation()[0].getNumberDecimal(), m.getLocation()[1].getNumberDecimal(), m.getName(), m.getAddress());
                            item.setId(m.get_id());
                            //mMap.addMarker(new MarkerOptions().position(pos).title(m.getName()).snippet(m.get_id()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            if (manager != null)
                                manager.addItem(item);
                        }
                    }
                }
            });


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
                    //if (position < 0) {
                        Uri uri = Uri.parse("/museums/" + id);
                        i.setData(uri);
                    //}
                    MuseuActivity.curMuseum = museums[position];
                    startActivityForResult(i, 1 );
                }
            });

            manager.setRenderer(new CustomClusterRenderer(getActivity(), map, manager));
        }
    }
    private int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, requireActivity().getResources().getDisplayMetrics()));
    }

    private int getMPosition(String title){
        int i = 0;
        for(Museo m : museums){
            if (m.getName().equals(title)) return i;
            i++;
        }
        return -1;
    }
    private boolean isDarkMode() {
        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
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