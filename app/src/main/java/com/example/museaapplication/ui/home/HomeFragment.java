package com.example.museaapplication.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.MuseuActivity;
import com.example.museaapplication.R;

import java.io.ByteArrayOutputStream;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Drawable image;

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
        LinearLayout scrollPais = root.findViewById(R.id.layout_pais);
        for(int i = 0; i < 10; i++){
            Button b = new Button(scrollPais.getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(pixToDp(200), pixToDp(150));
            param.setMargins(pixToDp(10), 0, 0, 0);
            b.setLayoutParams(param);
            b.setBackground(getResources().getDrawable(R.drawable.mnac_default));
            image = b.getBackground();
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "Museum Clicked", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), MuseuActivity.class);
                    SingletonDataHolder.getInstance().setCodedImage(imageToString(R.drawable.mnac_default));
                    startActivity(i);
                }
            });
            scrollPais.addView(b);
        }
        return root;
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
}