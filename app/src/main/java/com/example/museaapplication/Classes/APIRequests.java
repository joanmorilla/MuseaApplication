package com.example.museaapplication.Classes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.museaapplication.Classes.Json.Exhibition;
import com.example.museaapplication.Classes.Json.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
                Log.d("AA","" + museums[0].getExhibitions());
                SingletonDataHolder.getInstance().setMuseums(museums);
                CacheObjects();
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

    public void getExhibitions(Museo m, String id_exh) {
        Call<Exhibition> call = RetrofitClient.getInstance().getMyApi().getExhibition(m.get_id(), id_exh);
        call.enqueue(new Callback<Exhibition>() {
            @Override
            public void onResponse(Call<Exhibition> call, Response<Exhibition> response) {
                Exhibition exh = response.body();
                m.addExhibition(exh);
                Log.d("Exhibitions", exh.getName());
            }

            @Override
            public void onFailure(Call<Exhibition> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void CacheObjects() {
        Museo[] museums = SingletonDataHolder.getInstance().getMuseums();
        for (Museo m: museums){
            Log.d("BB", ""+ m.getLocation()[0].getNumberDecimal());
            m.setExhibitionObjects(new ArrayList<>());
            if (m.getExhibitions() != null)
                getExhibitions(m, m.getExhibitions()[0]);
        }
    }
}
