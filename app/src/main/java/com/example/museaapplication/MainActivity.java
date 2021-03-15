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

    final Fragment mHomeFragment = new HomeFragment();
    final Fragment mDashboardFragment = new DashboardFragment();
    final Fragment mNotificationsFragment = new NotificationsFragment();
    final Fragment mUserFragment = new UserFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mHomeFragment;
    BottomNavigationView navView;
    
    Stack<Integer> backStack = new Stack<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backStack.push(R.id.navigation_home);
        for (Fragment f: fm.getFragments()) {   
            fm.beginTransaction().remove(f).commit();
        }


        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.nav_host_fragment, mHomeFragment, "0").hide(mHomeFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mDashboardFragment, "1").hide(mDashboardFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mNotificationsFragment, "2").hide(mNotificationsFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, mUserFragment, "3").hide(mUserFragment).commit();

        selectIniFrag();

        navView = findViewById(R.id.nav_view);
        setTitle("Menu Principal");
        // Definimos comportamiento de la barra de navegación
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment fragment = null;

                /*if (!backStack.isEmpty() && !backStack.lastElement().equals(item.getItemId()))
                    backStack.push(item.getItemId());*/
                if (backStack.isEmpty()) backStack.push(item.getItemId());
                else {
                    if (!backStack.lastElement().equals(item.getItemId())) backStack.push(item.getItemId());
                }

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(mHomeFragment)/*.addToBackStack("HomeFragment")*/.commit();
                        active = mHomeFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 0;
                        setTitle("Menu Principal");
                        //backStack.push(R.id.navigation_home);
                        return true;
                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(mDashboardFragment)/*.addToBackStack("DashFragment")*/.commit();
                        active = mDashboardFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 1;
                        setTitle("Buscar");
                        /*if (!backStack.isEmpty() && !backStack.lastElement().equals(R.id.navigation_dashboard))
                            backStack.push(R.id.navigation_dashboard);
                        else if (backStack.isEmpty()) backStack.push(R.id.navigation_dashboard);*/
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(mNotificationsFragment)/*.addToBackStack("NotFragment")*/.commit();
                        active = mNotificationsFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 2;
                        setTitle("Notifications");
                        //backStack.push(R.id.navigation_notifications);
                        return true;
                    case R.id.navigation_user:
                        fm.beginTransaction().hide(active).show(mUserFragment)/*.addToBackStack("UserFragment")*/.commit();
                        active = mUserFragment;
                        SingletonDataHolder.getInstance().main_initial_frag = 3;
                        setTitle("Usuari");
                        /*if (!backStack.lastElement().equals(R.id.navigation_user))
                            backStack.push(R.id.navigation_user);*/
                        return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onBackPressed() {
        if (backStack.isEmpty()) Log.d("State", "isEmpty");
        else {
            backStack.pop();
            if (!backStack.isEmpty()) {
                int i = backStack.pop();
                navView.setSelectedItemId(i);
                Log.d("Stack2", "" + navView.getSelectedItemId());
                Log.d("Stack", "" + i);
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