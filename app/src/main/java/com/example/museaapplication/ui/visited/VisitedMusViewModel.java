package com.example.museaapplication.ui.visited;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.User;
import com.example.museaapplication.Classes.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitedMusViewModel extends ViewModel {

    public void addVisitedMuseum(String id_museum, String id_usuari) {
        Call<Void> call = RetrofitClient.getInstance().getMyApi().addVisitedMuseum(id_museum,id_usuari);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.d("toString",response.toString());
                Log.d("code","" + response.code());



            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();

            }


        });
    }
}