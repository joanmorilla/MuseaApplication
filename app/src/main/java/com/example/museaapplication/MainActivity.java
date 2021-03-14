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

public class MainActivity extends AppCompatActivity {

    final Fragment mHomeFragment = new HomeFragment();
    final Fragment mDashboardFragment = new DashboardFragment();
    final Fragment mNotificationsFragment = new NotificationsFragment();
    final Fragment mUserFragment = new UserFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mHomeFragment;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (Fragment f: fm.getFragments()) {
            fm.beginTransaction().remove(f).commit();
        }


        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.nav_host_fragment, mHomeFragment, "0").hide(mHomeFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mDashboardFragment, "1").hide(mDashboardFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mNotificationsFragment, "2").hide(mNotificationsFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mUserFragment, "3").hide(mUserFragment).commit();

        selectIniFrag();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        setTitle("Menu Principal");
        // Definimos comportamiento de la barra de navegación
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(mHomeFragment).commit();
                        active = mHomeFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 0;
                        setTitle("Menu Principal");
                        return true;
                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(mDashboardFragment).commit();
                        active = mDashboardFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 1;
                        setTitle("Buscar");
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(mNotificationsFragment).commit();
                        active = mNotificationsFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 2;
                        setTitle("Notifications");
                        return true;
                    case R.id.navigation_user:
                        fm.beginTransaction().hide(active).show(mUserFragment).commit();
                        active = mUserFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 3;
                        setTitle("Usuari");
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        super.onApplyThemeResource(theme, resid, first);
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