package com.example.museaapplication;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.ui.UserFragment;
import com.example.museaapplication.ui.dashboard.DashboardFragment;
import com.example.museaapplication.ui.home.HomeFragment;
import com.example.museaapplication.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    // Fragmentos del hub de navegación inferior
    final Fragment mHomeFragment = new HomeFragment();
    final Fragment mDashboardFragment = new DashboardFragment();
    final Fragment mNotificationsFragment = new NotificationsFragment();
    final Fragment mUserFragment = new UserFragment();
    // Cogemos el fragment manager e inicializamos estado activo
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mHomeFragment;

    BottomNavigationView navView;
    // Pila de ventanas para volver en orden adecuado


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingletonDataHolder.getInstance().backStack.push(R.id.navigation_home);
        for (Fragment f: fm.getFragments()) {   
            fm.beginTransaction().remove(f).commit();
        }

        // Inicialización del fragment manager
        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.nav_host_fragment, mHomeFragment, "0").hide(mHomeFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mDashboardFragment, "1").hide(mDashboardFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mNotificationsFragment, "2").hide(mNotificationsFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mUserFragment, "3").hide(mUserFragment).commit();
        // Mantenemos el estado al recargar la activity (cambio de tema)
        selectIniFrag();
        navView = findViewById(R.id.nav_view);
        // Definimos comportamiento de la barra de navegación
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Por cada click añadimos el elemento solo si la pila esta vacía o el ultimo elemento no es el item seleccionado
                if (SingletonDataHolder.getInstance().backStack.isEmpty()) SingletonDataHolder.getInstance().backStack.push(item.getItemId());
                else {
                    // Elimina el elemento de la pila si ya esta dentro antes de insertarlo de nuevo
                    // Asi limitamos el tamaño de la pila a la cantidad de ventanas
                    if (SingletonDataHolder.getInstance().backStack.contains(item.getItemId())){
                        int index = SingletonDataHolder.getInstance().backStack.indexOf(item.getItemId());
                        SingletonDataHolder.getInstance().backStack.remove(index);
                    }
                    if (!SingletonDataHolder.getInstance().backStack.lastElement().equals(item.getItemId())) SingletonDataHolder.getInstance().backStack.push(item.getItemId());
                }

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(mHomeFragment).commit();
                        active = mHomeFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 0;
                        setTitle(R.string.title_home);
                        return true;
                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(mDashboardFragment).commit();
                        active = mDashboardFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 1;
                        setTitle(R.string.title_dashboard);
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(mNotificationsFragment).commit();
                        active = mNotificationsFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 2;
                        setTitle(R.string.title_notifications);
                        return true;
                    case R.id.navigation_user:
                        fm.beginTransaction().hide(active).show(mUserFragment).commit();
                        active = mUserFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 3;
                        setTitle(R.string.title_user);
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
        }
    }

    void selectIniFrag(){
        switch (SingletonDataHolder.getInstance().main_initial_frag){
            case 1:
                active = mDashboardFragment;
                break;
            case 2:
                active = mNotificationsFragment;
                break;
            case 3:
                active = mUserFragment;
                break;
            default:
                active = mHomeFragment;
                break;
        }
        fm.beginTransaction().show(active).commit();
    }
}