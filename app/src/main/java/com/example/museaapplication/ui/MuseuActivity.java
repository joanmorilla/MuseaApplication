package com.example.museaapplication.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.ExpositionsList;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MuseuActivity extends AppCompatActivity {

    SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museu);

        // Setting the action bar buttons
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // For testing
        Picasso.get().setLoggingEnabled(true);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.setmMuseoFragment(new MuseoFragment());
        sharedViewModel.setmExpositionFragment(new ExpositionFragment());

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragment_container, sharedViewModel.getmExpositionFragment(), "1").hide(sharedViewModel.getmExpositionFragment()).commit();
        fm.beginTransaction().add(R.id.fragment_container, sharedViewModel.getmMuseoFragment(), "0").hide(sharedViewModel.getmMuseoFragment()).commit();
        fm.beginTransaction().show(sharedViewModel.getActive()).commit();

        Bundle b = getIntent().getExtras();
        Museo museum = (Museo)b.getSerializable("Museu");

        sharedViewModel.setCurMuseum(museum);
    }

    // create an action bar button
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res

        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public void onBackPressed() {
        SignalFragments();
        //super.onBackPressed();
    }

    private void SignalFragments(){
        ((OnBackPressed)sharedViewModel.getActive()).OnBack();
    }
}