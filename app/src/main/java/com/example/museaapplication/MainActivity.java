package com.example.museaapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

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
        setContentView(R.layout.activity_main);

        fm.beginTransaction().add(R.id.nav_host_fragment, mHomeFragment, "0").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mDashboardFragment, "1").hide(mDashboardFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mNotificationsFragment, "2").hide(mNotificationsFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mUserFragment, "3").hide(mUserFragment).commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        setTitle("Menu Principal");

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(mHomeFragment).commit();
                        active = mHomeFragment;
                        setTitle("Menu Principal");
                        return true;
                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(mDashboardFragment).commit();
                        active = mDashboardFragment;
                        setTitle("Buscar");
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(mNotificationsFragment).commit();
                        active = mNotificationsFragment;
                        setTitle("Notifications");
                        return true;
                    case R.id.navigation_user:
                        fm.beginTransaction().hide(active).show(mUserFragment).commit();
                        active = mUserFragment;
                        setTitle("Usuari");
                        return true;
                }
                //loadFragment(ac);
                return false;
            }
        });



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_user)
                .build();

        //loadFragment(new HomeFragment());

        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}