package com.example.museaapplication.ui.visited;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Dominio.Visited;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.edit.edit_userViewModel;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class VisitedMus extends AppCompatActivity {

    Button scanbtn;
    public static TextView scantext;
    public static String id_museum;
    public static int numvisited = 1;
    public static String[] images;
    ArrayList<String> listids;
    VisitedMusViewModel vmvm;
    String name_m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_mus);
        scanbtn = findViewById(R.id.scan_button);
        vmvm = new ViewModelProvider(this).get(VisitedMusViewModel.class);


        vmvm.getVisited().observe(this, new Observer<Visited[]>() {
            @Override
            public void onChanged(Visited[] visited) {
                LinearLayout ll = findViewById(R.id.linearlayout_visited);
                ll.removeAllViews();
                //vmvm.loadMuseums();
                vmvm.getMuseos().observe(VisitedMus.this, new Observer<ArrayList<Museo>>() {
                    @Override
                    public void onChanged(ArrayList<Museo> museos) {
                        Log.e("FORactivity", String.valueOf(museos.size()));
                        ll.removeAllViews();
                        for (int index = museos.size() - 1; index >= 0; index--) {
                            RelativeLayout holder = new RelativeLayout(getApplicationContext());
                            View v = View.inflate(getApplicationContext(), R.layout.item_visited, holder);
                            TextView museum = v.findViewById(R.id.idNombre);
                            TextView pais = v.findViewById(R.id.idCiudad);
                            TextView ciudad = v.findViewById(R.id.idPais);
                            ImageView iv = v.findViewById(R.id.id_image);
                            museum.setText(museos.get(index).getName());


                            ciudad.setText(museos.get(index).getCity());


                            Picasso.get().load(museos.get(index).getImage()).into(iv);
                            Log.e("FOTO",museos.get(index).getImage());
                            ll.addView(v);
                        }
                    }
                    });
                }
            });


        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }

        });
        }
    }
