package com.example.museaapplication.ui.favourite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museaapplication.Classes.Adapaters.Favourite_museosAdapter;
import com.example.museaapplication.Classes.Dominio.Favourites;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMus extends AppCompatActivity {

    List<Favourites> favList;
    RecyclerView recyclerMuseos;
    Favourite_museosAdapter adapter;
    Favourites[] favarr;
    private FavouriteMusViewModel fvm;
    TextView id;
    TextView image;
    ArrayList<String> listids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_mus);
        fvm = new ViewModelProvider(this).get(FavouriteMusViewModel.class);

        //id = findViewById(R.id.te_id);
        //image = findViewById(R.id.te_ima);
/*
        fvm.getFavourites().observe(this,favourites ->  {
            favarr = new Favourites[favourites.length];
            favarr = favourites;
            id.setText(favourites[0].museumId());
            image.setText(favourites[0].image());
        });*/

        listids = getIntent().getStringArrayListExtra("id");

        LinearLayout llimages = findViewById(R.id.layout_fav);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );

        for (int i=0; i<listids.size(); i++){
            Button button = new Button(this);
            ImageView iv = new ImageView(this);
            //Asignamos propiedades de layout al boton
            button.setLayoutParams(lp);
            //Asignamos Texto al botón
            button.setText(listids.get(i));
            //Añadimos el botón a la botonera
            llimages.addView(button);
        }


    }



    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()));
    }
    /*
    private void fill_list(String s) {
        String lowerText = s.toLowerCase();
        favList = new ArrayList<>();
        for (Favourites f : favarr) {
            favList.add(f);

        }
        Favourite_museosAdapter adapter = new Favourite_museosAdapter(favList);
        recyclerMuseos.setAdapter(adapter);


    }*/

    /*
    private void initViews(){
        favList = findViewById(R.id.recyclerIdfav);
    }

    private void initValues(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerMuseos.setLayoutManager(manager);


    }*/
}