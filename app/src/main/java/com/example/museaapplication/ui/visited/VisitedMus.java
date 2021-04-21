package com.example.museaapplication.ui.visited;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.museaapplication.R;
import com.example.museaapplication.scannerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VisitedMus extends AppCompatActivity {

    Button scanbtn;
    public static TextView scantext;
    public static String id_museum;
    public static int numvisited=1;
    public static String[] images;
    ArrayList<String> listids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_mus);
        scantext = findViewById(R.id.scan_text);
        scanbtn = findViewById(R.id.scan_button);

        //listids = getIntent().getStringArrayListExtra("vis");

        LinearLayout llimages = findViewById(R.id.layout_vis);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );

        for (int i=0; i<4; i++){
            Button button = new Button(this);
            ImageView iv = new ImageView(this);
            //Asignamos propiedades de layout al boton
            button.setLayoutParams(lp);
            //Asignamos Texto al botón
            button.setText("1");
            //Añadimos el botón a la botonera
            llimages.addView(button);
        }

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }

        });
    }

}