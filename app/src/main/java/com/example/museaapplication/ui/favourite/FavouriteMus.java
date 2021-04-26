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

        listids = getIntent().getStringArrayListExtra("image");

       /* LinearLayout llimages = findViewById(R.id.layout_fav);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );*/
        generar_likes();
/*
        for (int i=0; i<listids.size(); i++){
            Button button = new Button(this);
            ImageView iv = new ImageView(this);
            //Asignamos propiedades de layout al boton
            button.setLayoutParams(lp);
            //Asignamos Texto al botón
            button.setText(listids.get(i));
            Picasso.get().load(listids.get(i)).fit().centerCrop().into(iv);
            //Añadimos el botón a la botonera
            llimages.addView(iv);
        }*/



    }

    private void generar_likes(){

        LinearLayout scrollPais = findViewById(R.id.layout_fav);
        for(int i = listids.size() -1; i >= 0; i--){
            // Generamos boton
            ImageButton b = new ImageButton(scrollPais.getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(pixToDp(383), pixToDp(200));
            param.setMargins(pixToDp(5), 30, pixToDp(5), 0);
            b.setLayoutParams(param);
            b.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.drawable_button));
            // Le asignams la imagen del museo en cuestion
            Picasso.get().load(listids.get(i)).fit().centerCrop().into(b);
            // Asignamos un comportamiento para cuando se presione
            // Finalmente lo añadimos a la vista desplazable
            scrollPais.addView(b);
        }
    }






    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()));
    }

}