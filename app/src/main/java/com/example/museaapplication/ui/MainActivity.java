package com.example.museaapplication.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.Map.MapFragment;
import com.example.museaapplication.ui.home.HomeFragment;
import com.example.museaapplication.ui.home.HomeViewModel;
import com.example.museaapplication.ui.notifications.NotificationsFragment;

import com.example.museaapplication.ui.user.UserViewModel;

import com.example.museaapplication.ui.search.SearchFragment;
import com.example.museaapplication.ui.user.UserFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Fragmentos del hub de navegación inferior
    final Fragment mHomeFragment = new HomeFragment();
    //final Fragment mDashboardFragment = new DashboardFragment();
    final Fragment mSearchFragment = new SearchFragment();
    final Fragment mMapFragment = new MapFragment();
    Fragment mNotificationsFragment = new NotificationsFragment();

    Fragment mUserFragment = new UserFragment();
    // Cogemos el fragment manager e inicializamos estado activo
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mHomeFragment;

    TextView txt;

    BottomNavigationView navView;
    private MapView mMapView;

    HomeViewModel hvm;


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AWSMobileClient.getInstance().initialize(this).execute();

        mMapView = findViewById(R.id.map_view);
        hvm = new ViewModelProvider(this).get(HomeViewModel.class);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            //SingletonDataHolder.getInstance().backStack.push(R.id.navigation_home);
        for (Fragment f: fm.getFragments()) {   
            fm.beginTransaction().remove(f).commit();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.appbar_layout_test);
        actionBar.setShowHideAnimationEnabled(true);

        SingletonDataHolder.userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Inicialización del fragment manager
        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.nav_host_fragment, mHomeFragment, "0").hide(mHomeFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mSearchFragment, "1").hide(mSearchFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mMapFragment, "4").hide(mMapFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mNotificationsFragment, "2").hide(mNotificationsFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mUserFragment, "3").hide(mUserFragment).commit();
        // Mantenemos el estado al recargar la activity (cambio de tema)
        navView = findViewById(R.id.nav_view);
        //navView.setSelectedItemId(R.id.navigation_home);
        txt = findViewById(R.id.title_test);
        selectIniFrag();
        if (!SingletonDataHolder.first) {
            SingletonDataHolder.first = true;
            SingletonDataHolder.backStack.push(R.id.navigation_home);
        }
        // Definimos comportamiento de la barra de navegación
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Por cada click añadimos el elemento solo si la pila esta vacía o el ultimo elemento no es el item seleccionado
                if (SingletonDataHolder.getInstance().backStack.isEmpty()) {
                    SingletonDataHolder.getInstance().backStack.push(item.getItemId());
                }
                else {
                    // Elimina el elemento de la pila si ya esta dentro antes de insertarlo de nuevo
                    // Asi limitamos el tamaño de la pila a la cantidad de ventanas
                    if (SingletonDataHolder.getInstance().backStack.contains(item.getItemId())){
                        int index = SingletonDataHolder.getInstance().backStack.indexOf(item.getItemId());
                        SingletonDataHolder.getInstance().backStack.remove(index);
                    }
                    if (!SingletonDataHolder.getInstance().backStack.isEmpty() && !SingletonDataHolder.getInstance().backStack.lastElement().equals(item.getItemId())) SingletonDataHolder.getInstance().backStack.push(item.getItemId());
                    else if (SingletonDataHolder.getInstance().backStack.isEmpty()) SingletonDataHolder.getInstance().backStack.push(item.getItemId());
                }
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(mHomeFragment).commit();
                        active = mHomeFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 0;
                        txt.setText(R.string.title_home);
                        txt.setClickable(true);
                        getSupportActionBar().show();
                        txt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Funca", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return true;
                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(mSearchFragment).commit();
                        active = mSearchFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 1;
                        txt.setText(R.string.title_dashboard);
                        txt.setClickable(false);
                        getSupportActionBar().show();
                        return true;
                    case R.id.navigation_maps:
                        fm.beginTransaction().hide(active).show(mMapFragment).commit();
                        active = mMapFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 4;
                        txt.setText(R.string.title_maps);
                        txt.setClickable(false);
                        getSupportActionBar().hide();
                        return true;
                    case R.id.navigation_notifications:
                        mNotificationsFragment = new NotificationsFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, mNotificationsFragment, "2").hide(active).show(mNotificationsFragment).commit();
                        active = mNotificationsFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 2;
                        txt.setText(R.string.title_notifications);
                        getSupportActionBar().show();
                        txt.setClickable(false);
                        return true;
                    case R.id.navigation_user:
                        mUserFragment = new UserFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, mUserFragment, "3").hide(active).show(mUserFragment).commit();
                        active = mUserFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 3;
                        txt.setText(R.string.title_user);
                        getSupportActionBar().show();
                        txt.setClickable(false);
                        return true;
                }
                return false;
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onBackPressed() {
        if (!SingletonDataHolder.getInstance().backStack.isEmpty()){
            // Cada vez que presionamos añadimos a stack
            // Aqui quitamos el estado actual antes de volver al anterior
            SingletonDataHolder.getInstance().backStack.pop();
            if (!SingletonDataHolder.getInstance().backStack.isEmpty()) {
                int i = SingletonDataHolder.getInstance().backStack.pop();
                navView.setSelectedItemId(i);
            } else super.onBackPressed();
        }else super.onBackPressed();
    }

    void selectIniFrag(){
        switch (SingletonDataHolder.getInstance().main_initial_frag){
            case 1:
                active = mSearchFragment;
                txt.setText(R.string.title_dashboard);
                txt.setClickable(false);
                navView.setSelectedItemId(R.id.navigation_dashboard);
                break;
            case 2:
                active = mNotificationsFragment;
                txt.setText(R.string.title_notifications);
                txt.setClickable(false);
                navView.setSelectedItemId(R.id.navigation_notifications);
                break;
            case 3:
                active = mUserFragment;
                txt.setText(R.string.title_user);
                txt.setClickable(false);
                navView.setSelectedItemId(R.id.navigation_user);
                break;
            case 4:
                active = mMapFragment;
                txt.setText(R.string.title_maps);
                txt.setClickable(false);
                getSupportActionBar().hide();
                navView.setSelectedItemId(R.id.navigation_maps);
                break;
            default:
                active = mHomeFragment;
                //fm.beginTransaction().show(active).commit();
                txt.setText(R.string.title_home);
                txt.setClickable(true);
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Funca", Toast.LENGTH_SHORT).show();
                    }
                });
                navView.setSelectedItemId(R.id.navigation_home);
                break;
        }
        //navView.setSelectedItemId(R.id.navigation_home);
        fm.beginTransaction().show(active).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean changed = data.getBooleanExtra("Changed", false);
        boolean result = data.getBooleanExtra("Value", true);
        if (changed) {
            if (result){
                hvm.newFavourite(MuseuActivity.curMuseum);
            }else hvm.unFavorite(MuseuActivity.curMuseum);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            mHomeFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            mMapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}