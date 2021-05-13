package com.example.museaapplication.ui.visited;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
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
                int i = 0;
                for (int index = visited.length - 1; index >= 0; index--) {
                    RelativeLayout holder = new RelativeLayout(getApplicationContext());
                    View v = View.inflate(getApplicationContext(), R.layout.custom_visited, holder);
                    TextView museum = v.findViewById(R.id.museum_name_visited);
                   /* vmvm.getMuseobyid(visited[index].museumId()).observe(VisitedMus.this, new Observer<Museo[]>() {
                        @Override
                        public void onChanged(Museo[] museos) {
                            Log.e("MUSEOOOO", String.valueOf(museos.length));
                            name_m = museos[0].getName();
                        }
                    });*/
                    museum.setText(visited[index].museumId());
                    Log.e("IDDDDDDDDD", visited[index].museumId());
                    ImageView iv = v.findViewById(R.id.museum_image_visited);
                    Log.e("IDDDDDDDDD", visited[index].image());
                    Picasso.get().load(visited[index].image()).into(iv);
                    ll.addView(v);
                }
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