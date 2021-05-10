package com.example.museaapplication.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

public class MuseuActivity extends AppCompatActivity {

    public static Museo curMuseum;


    SharedViewModel sharedViewModel;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museu);
        //StatusBarUtil.set
        // For testing
        Picasso.get().setLoggingEnabled(true);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.setmMuseoFragment(new MuseoFragment());
        sharedViewModel.setmExpositionFragment(new ExpositionFragment());
        sharedViewModel.setmCommentsFragment(new Comentaris_Fragment());


        fm = getSupportFragmentManager();

        fm.beginTransaction().add(R.id.fragment_container, sharedViewModel.getmExpositionFragment(), "1").hide(sharedViewModel.getmExpositionFragment()).commit();
        fm.beginTransaction().add(R.id.fragment_container, sharedViewModel.getmMuseoFragment(), "0").hide(sharedViewModel.getmMuseoFragment()).commit();
        fm.beginTransaction().add(R.id.fragment_container, sharedViewModel.getmCommentsFragment(), "2").hide(sharedViewModel.getmCommentsFragment()).commit();
        fm.beginTransaction().show(sharedViewModel.getActive()).commit();

        if (getIntent().getData() != null) {
            Uri data = getIntent().getData();
            /*Log.d("URIII", "" + data.getPath());
            Log.d("ID", data.getPath().replace("/museums/", ""));*/
            sharedViewModel.loadMuseum(data.getPath().replace("/museums/", ""));
        }else {
            // Keep it cutre. Gracias google por copiar los datos en nuevas variables al iniciar una activity cuando
            // java pasa referencias. Muy útil.
            sharedViewModel.setCurMuseum(curMuseum);
            sharedViewModel.setMyMuseum(curMuseum);
        }
    }

    // create an action bar button
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res

        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    protected void onStop() {
        fm.beginTransaction().hide(sharedViewModel.getmExpositionFragment()).commit();
        fm.beginTransaction().hide(sharedViewModel.getmMuseoFragment()).commit();
        fm.beginTransaction().hide(sharedViewModel.getmCommentsFragment()).commit();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fm.beginTransaction().show(sharedViewModel.getActive()).commit();
    }

    @Override
    protected void onDestroy() {
        /*fm.beginTransaction().hide(sharedViewModel.getmExpositionFragment()).commit();
        fm.beginTransaction().hide(sharedViewModel.getmMuseoFragment()).commit();
        fm.beginTransaction().hide(sharedViewModel.getmCommentsFragment()).commit();*/
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        SignalFragments();
        //super.onBackPressed();
    }

    private void SignalFragments(){
        ((OnBackPressed)sharedViewModel.getActive()).OnBack();
    }
}