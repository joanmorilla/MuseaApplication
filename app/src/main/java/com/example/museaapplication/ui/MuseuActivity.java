package com.example.museaapplication.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Dominio.Work;
import com.example.museaapplication.Classes.Json.AuxMuseo;
import com.example.museaapplication.Classes.Json.ExpositionsList;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.WorksValue;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MuseuActivity extends AppCompatActivity {

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
            Log.d("URIII", "" + data.getPath());
            Log.d("ID", data.getPath().replace("/museums/", ""));
            sharedViewModel.loadMuseum(data.getPath().replace("/museums/", ""));
        }else {
            Bundle b = getIntent().getExtras();
            Museo museum = (Museo) b.getSerializable("Museu");

            sharedViewModel.setCurMuseum(museum);
            sharedViewModel.setMyMuseum(museum);
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
        //fm.beginTransaction().show(sharedViewModel.getActive()).commit();
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