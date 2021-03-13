package com.example.museaapplication.Classes;

import android.util.Log;

import com.example.museaapplication.Classes.Json.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIRequests {
    private static APIRequests _instance;
    public static APIRequests getInstance(){
        if (_instance == null) _instance = new APIRequests();
        return _instance;
    }


    public void getAllMuseums(Delegate function) {
        Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseums("Louvre");
        call.enqueue(new Callback<MuseoValue>() {
            @Override
            public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                MuseoValue mymuseumList = response.body();
                Museo[] museums = mymuseumList.getMuseums();
                SingletonDataHolder.getInstance().setMuseums(museums);
                if (function != null)
                    function.Execute();
            }

            @Override
            public void onFailure(Call<MuseoValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
                SingletonDataHolder.getInstance().setMuseums(null);
                getAllMuseums(function);
            }
        });
    }
}
