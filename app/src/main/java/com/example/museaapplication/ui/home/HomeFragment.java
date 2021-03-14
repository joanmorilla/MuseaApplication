package com.example.museaapplication.ui.home;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Delegate;
import com.example.museaapplication.Classes.Json.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.MuseuActivity;
import com.example.museaapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    boolean interactable = true;
    View root;

    Museo[] museus;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Ejecucion asincrona, obetenemos museos y generamos botones
        APIRequests.getInstance().getAllMuseums(new Delegate() {
            @Override
            public void Execute() {
                GenerarBotones();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void getMuseums(){
        Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseums("Louvre");
        call.enqueue(new Callback<MuseoValue>() {
            @Override
            public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                MuseoValue mymuseumList = response.body();
                Museo[] museums = mymuseumList.getMuseums();
                museus = new Museo[museums.length];
                for (int i = 0; i < museums.length; i++){
                    museus[i] = museums[i];
                    Log.d("museos",museums[i].getName());
                }
                GenerarBotones();
                //Toast.makeText(getContext(), strings[1], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MuseoValue> call, Throwable t) {
                //Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
                getMuseums();
            }
        });
    }
    void GenerarBotones() {
        LinearLayout scrollPais = root.findViewById(R.id.layout_pais);
        Museo[] museums = SingletonDataHolder.getInstance().getMuseums();
        for(int i = museums.length - 1; i >= 0; i--){
            Button b = new Button(scrollPais.getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(pixToDp(200), pixToDp(150));
            param.setMargins(pixToDp(10), 0, 0, 0);
            b.setLayoutParams(param);
            b.setBackground(root.getResources().getDrawable(R.drawable.mnac_default));
            b.setOnClickListener(clickFunc(i));
            scrollPais.addView(b);
        }
    }
    View.OnClickListener clickFunc(int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interactable) {
                    Intent i = new Intent(getContext(), MuseuActivity.class);
                    SingletonDataHolder.getInstance().setCodedImage(imageToString(R.drawable.mnac_default));
                    Bundle bundle = new Bundle();
                    i.putExtra("Museu", (Serializable) SingletonDataHolder.getInstance().getMuseums()[index]);
                    startActivity(i);
                    interactable = false;
                }
            }
        };
    }
}
