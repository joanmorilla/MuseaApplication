package com.example.museaapplication.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.MuseuActivity;
import com.example.museaapplication.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Drawable image;
    private boolean interactable = true;
    int j = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Request
        Button button = root.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMuseums();
            }
        });



        LinearLayout scrollPais = root.findViewById(R.id.layout_pais);
        for(int i = 0; i < 10; i++){
            Button b = new Button(scrollPais.getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(pixToDp(200), pixToDp(150));
            param.setMargins(pixToDp(10), 0, 0, 0);
            b.setLayoutParams(param);

            image = b.getBackground();
            if (i % 2 == 0) {
                b.setBackground(getResources().getDrawable(R.drawable.mnac_default));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getContext(), "Museum Clicked", Toast.LENGTH_SHORT).show();
                        if (interactable) {
                            Intent i = new Intent(getContext(), MuseuActivity.class);
                                SingletonDataHolder.getInstance().setCodedImage(imageToString(R.drawable.mnac_default));
                                i.putExtra("Name", "Mnac");
                            startActivity(i);
                            interactable = false;
                        }
                    }
                });
            }else{
                b.setBackground(getResources().getDrawable(R.drawable.louvre_default));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getContext(), "Museum Clicked", Toast.LENGTH_SHORT).show();
                        if (interactable) {
                            Intent i = new Intent(getContext(), MuseuActivity.class);
                            SingletonDataHolder.getInstance().setCodedImage(imageToString(R.drawable.louvre_default));
                            i.putExtra("Name", "Louvre");
                            startActivity(i);
                            interactable = false;
                        }
                    }
                });
            }
            scrollPais.addView(b);
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        interactable = true;
    }

    protected void onRestart() {

    }

    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()));
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
        Call<List<Museo>> call = RetrofitClient.getInstance().getMyApi().getMuseums();
        call.enqueue(new Callback<List<Museo>>() {
            @Override
            public void onResponse(Call<List<Museo>> call, Response<List<Museo>> response) {
                List<Museo> mymuseumList = response.body();
                String[] oneMuseum = new String[mymuseumList.size()];

                for (int i = 0; i < mymuseumList.size(); i++) {
                    oneMuseum[i] = mymuseumList.get(i).getName();
                }
                Toast.makeText(getContext(), oneMuseum[0], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<Museo>> call, Throwable t) {
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                Log.d("Causa","" + t.getCause());
            }
        });
    }
}