package com.example.museaapplication.ui.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.example.museaapplication.Classes.Dominio.Likes;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.SettingsActivity;

import com.example.museaapplication.ui.visited.VisitedMus;
import com.example.museaapplication.ui.edit.edit_user;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    private UserViewModel uvm;
    private String name;
    ArrayList<String> mus_favs_id;
    ArrayList<String> mus_favs_image;
    ArrayList<String> visited;
    String[] mus_vis;
    String[] mus_vis_id;
    String[] mus_vis_image;

    Likes[] work_likes;

    View root;
    String username;
    String password;



    public UserFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Toast toast = Toast.makeText(getContext(), name, Toast.LENGTH_SHORT);
        //toast.show();
        Log.e("Creado", "la creaacion");
        uvm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        SingletonDataHolder.userViewModel = uvm;
        root = inflater.inflate(R.layout.fragment_user, container, false);
        Button btn = root.findViewById(R.id.button_eu);
        TextView user_name = root.findViewById(R.id.user_name);
        TextView user_bio = root.findViewById(R.id.user_bio);
        TextView visited_m = root.findViewById(R.id.visited_mus);
        TextView fav_m = root.findViewById(R.id.favourties);
        //TextView id = root.findViewById(R.id.t_id);
        //TextView image = root.findViewById(R.id.t_image);
        TextView fav_n = root.findViewById(R.id.favourties);
        TextView vis_n = root.findViewById(R.id.visited_mus);
        TextView points = root.findViewById(R.id.points);
        CircularImageView circularImageView = root.findViewById(R.id.circularImageView);
        String url = "https://museaimages.s3.eu-west-3.amazonaws.com/logo.png";
        Picasso.get().load(url).fit().into(circularImageView);
        SingletonDataHolder.userViewModel = uvm;

        /*SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        String sharedValue = sharedPref.getString(getString(R.string.auto_signin_key), defaultValue);
        int index = sharedValue.lastIndexOf('#');
        username = (sharedValue.substring(0,index));
        password = (sharedValue.substring(index + 1));*/

        SharedPreferences sharedPref = getActivity().getSharedPreferences("infouser",0);
        String defaultValue = "";
        String sharedValue = sharedPref.getString(getString(R.string.auto_signin_key), defaultValue);
            //int index = sharedValue.lastIndexOf('#');
            Log.d("SharedPreferences2","Login automatico: " + sharedValue);
           /* Log.d("SharedPreferences2",sharedValue.substring(0,index));
            Log.d("SharedPreferences2",sharedValue.substring(index + 1));
            username = (sharedValue.substring(0,index));
            password = (sharedValue.substring(index+1));*/
        uvm.getinfoUser().observe(getViewLifecycleOwner(), new Observer<UserInfo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(UserInfo userInfo) {
                user_name.setText(userInfo.getName());
                user_bio.setText(userInfo.getBio());
                String n = String.valueOf(userInfo.getFavourites().length);
                Log.e("FAVVVVV", String.valueOf(userInfo.getFavourites().length));
                fav_n.setText(n);
                String x = String.valueOf(userInfo.getVisited().length);
                vis_n.setText(x);
                points.setText(root.getResources().getString(R.string.points) + " " + userInfo.getPoints());
                String url = userInfo.getProfilePic();
                Picasso.get().load(url).fit().into(circularImageView);

            }

        });


        uvm.getLikes().observe(getViewLifecycleOwner(), new Observer<Likes[]>() {
            @Override
            public void onChanged(Likes[] likes) {
                work_likes = new Likes[likes.length];
                work_likes = likes;
                generar_likes();
            }
        });

        visited_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), VisitedMus.class);
                //intent.putStringArrayListExtra("vis",visited);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), edit_user.class);
                intent.putExtra("username", user_name.getText().toString());
                intent.putExtra("bio", user_bio.getText().toString());
                startActivity(intent);
            }
        });


        return root;
    }


    private void generar_likes(){
        LinearLayout scrollPais = root.findViewById(R.id.layout_likes);
        scrollPais.removeAllViews();
        for(int i = work_likes.length -1; i >= 0; i--){
            // Generamos boton
            ImageButton b = new ImageButton(scrollPais.getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(pixToDp(100), pixToDp(100));
            param.setMargins(pixToDp(5), 0, pixToDp(5), 0);
            b.setLayoutParams(param);
            b.setBackground(getContext().getResources().getDrawable(R.drawable.drawable_button));
            // Le asignams la imagen del museo en cuestion
            Picasso.get().load(work_likes[i].getImage()).fit().centerCrop().into(b);
            // Asignamos un comportamiento para cuando se presione
            // Finalmente lo añadimos a la vista desplazable
            scrollPais.addView(b);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.config){
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);

        }

    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, root.getResources().getDisplayMetrics()));
    }



}

