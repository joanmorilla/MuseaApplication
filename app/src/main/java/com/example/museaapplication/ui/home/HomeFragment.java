package com.example.museaapplication.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Delegate;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.ui.MuseuActivity;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

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
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView txt = super.getActivity().findViewById(R.id.title_test);
        txt.setText(R.string.title_home);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funca", Toast.LENGTH_SHORT).show();
            }
        });
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void GenerarBotones(Museo[] m) {
        LinearLayout scrollPais = root.findViewById(R.id.layout_pais);
        Museo[] museums = m;
        for(int i = museums.length - 1; i >= 0; i--){
            // Generamos boton
            ImageButton b = new ImageButton(scrollPais.getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(pixToDp(225), pixToDp(175));
            param.setMargins(pixToDp(5), 0, pixToDp(5), 0);
            b.setLayoutParams(param);
            b.setBackground(getContext().getResources().getDrawable(R.drawable.drawable_button));
            // Le asignams la imagen del museo en cuestion
            Picasso.get().load(m[i].getImage()).fit().centerCrop().into(b);

            // Asignamos un comportamiento para cuando se presione
            b.setOnClickListener(clickFunc(m[i]));
            // Finalmente lo añadimos a la vista desplazable
            scrollPais.addView(b);
        }
    }
    View.OnClickListener clickFunc(Museo m) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interactable) {
                    Intent i = new Intent(getContext(), MuseuActivity.class);
                    i.putExtra("Museu", (Serializable) m);
                    startActivity(i);
                    interactable = false;
                }
            }
        };
    }
}
