package com.example.museaapplication.ui.home;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Delegate;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.Classes.TimeClass;
import com.example.museaapplication.ui.MuseuActivity;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.material.resources.MaterialResources.getDrawable;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    boolean interactable = true;
    View root;

    Museo[] museus;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView txt = root.findViewById(R.id.title_test);
        //txt.setText(R.string.title_home);
        /*txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funca", Toast.LENGTH_SHORT).show();
            }
        });*/
        ProgressBar pb = (ProgressBar) root.findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        homeViewModel.getMuseums().observe(getViewLifecycleOwner(), new Observer<Museo[]>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChanged(Museo[] museos) {
                GenerarBotones(museos);
                pb.setVisibility(View.GONE);
            }

        });
        homeViewModel.getFavouriteMuseums().observe(getViewLifecycleOwner(), new Observer<Museo[]>() {
            @Override
            public void onChanged(Museo[] museos) {
                GenerateFavourites(museos);
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        interactable = true;
    }
    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, root.getResources().getDisplayMetrics()));
    }

    String imageToString(int id){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    Bitmap stringToImage(String codeImage){
        byte[] imageBytes = Base64.decode(codeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    void GenerarBotones(Museo[] m) {
        LinearLayout scrollPais = root.findViewById(R.id.layout_pais);
        Museo[] museums = m;
        // We go through the museums
        for(int i = museums.length - 1; i >= 0; i--){
            // For the complex button we use relative layout
            RelativeLayout holder = new RelativeLayout(getContext());
            View v = View.inflate(getContext(), R.layout.custom_button_layout, holder);
            // Adding enter animation
            YoYo.with(Techniques.ZoomIn)/*.delay((museums.length - i) * 200)*/.duration(700).playOn(v);
            TextView txt  = v.findViewById(R.id.white_rectangle);
            // Setting the texts in custom button
            txt.setText(m[i].getName());
            txt = v.findViewById(R.id.text_horari);
            if (m[i].getCovidInformation() != null){
                txt.setText(timeStringValidation(m[i].getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                m[i].setOpeningHour(parseOpeningHour(m[i].getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
            }
            txt = v.findViewById(R.id.text_pais);
            txt.setText(m[i].getCity());
            ImageButton ib = v.findViewById(R.id.image_view);
            ib.setOnClickListener(clickFunc(m[i]));
            if (!m[i].getImage().equals(""))
                Picasso.get().load(m[i].getImage()).fit().centerCrop().into(ib);
            // Size the relative layout
            RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, pixToDp(155));
            newParams.setMargins(pixToDp(5),0,pixToDp(5),pixToDp(0));
            holder.setLayoutParams(newParams);
            // Finally add it to the scroll layout
            scrollPais.addView(holder);
        }
    }
    private void GenerateFavourites(Museo[] m){
        LinearLayout scrollFavourites = root.findViewById(R.id.layout_favourites);
        // We go through the museums
        for(int i = m.length - 1; i >= 0; i--){
            // For the complex button we use relative layout
            RelativeLayout holder = new RelativeLayout(getContext());
            View v = View.inflate(getContext(), R.layout.custom_button_layout, holder);
            // Adding enter animation
            YoYo.with(Techniques.ZoomIn)/*.delay((museums.length - i) * 200)*/.duration(700).playOn(v);
            TextView txt  = v.findViewById(R.id.white_rectangle);
            // Setting the texts in custom button
            txt.setText(m[i].getName());
            txt = v.findViewById(R.id.text_horari);
            if (m[i].getCovidInformation() != null){
                txt.setText(timeStringValidation(m[i].getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                m[i].setOpeningHour(parseOpeningHour(m[i].getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
            }
            txt = v.findViewById(R.id.text_pais);
            txt.setText(m[i].getCity());
            ImageButton ib = v.findViewById(R.id.image_view);
            ib.setOnClickListener(clickFunc(m[i]));
            if (!m[i].getImage().equals(""))
                Picasso.get().load(m[i].getImage()).fit().centerCrop().into(ib);
            // Size the relative layout
            RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, pixToDp(155));
            newParams.setMargins(pixToDp(5),0,pixToDp(5),pixToDp(0));
            holder.setLayoutParams(newParams);
            // Finally add it to the scroll layout
            scrollFavourites.addView(holder);
        }
    }
    View.OnClickListener clickFunc(Museo m) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interactable) {
                    String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(TimeClass.getNow());
                    Intent i = new Intent(getContext(), MuseuActivity.class);
                    MuseuActivity.curMuseum = m;
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
                    interactable = false;
                }
            }
        };
    }
    private String timeStringValidation(String time){
        if (time.contains("Closed")) return "Closed";
        String myOpeningHour="", myClosingHour="", result;
        int index = time.indexOf(':');
        String myTime = time.substring(index).replace(": ", "").replace(" ", ""); // Now we have 10:30AM-8:00PM
        // We need to separate them into the two different hours
        String[] hours = myTime.split("–");
        // Validating both of them
        for (String hour : hours){
            if (hour.contains("PM")) {
                // We get the PM one and subdivide it in hours and minutes
                myClosingHour = hour.replace("PM", "");
                String[] h = myClosingHour.split(":");
                // We now have the numeric value of our hours
                int hs = Integer.parseInt(h[0].trim());
                hs+=12;
                myClosingHour = String.valueOf(hs) + ":" + h[1];
            }else myOpeningHour = hour.replace("AM", "");
        }
        // We then combine them
        result = myOpeningHour+"-"+myClosingHour;
        return result;
    }
    private int parseOpeningHour(String time){
        if (time.contains("Closed")) return -1;
        int index = time.indexOf(':');
        String myTime = time.substring(index).replace(": ", "").replace(" ", ""); // Now we have 10:30AM-8:00PM
        String[] hours = myTime.split("–");
        String temp = hours[0].replace("AM", ""); // 10:30
        String[] h = temp.split(":"); // [10, 30]
        return Integer.parseInt(h[0]);
    }
}
