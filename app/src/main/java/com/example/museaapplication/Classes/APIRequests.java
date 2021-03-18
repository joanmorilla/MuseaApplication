package com.example.museaapplication.Classes;

import android.util.Log;

import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Work;
import com.example.museaapplication.Classes.Json.ExhibitionValue;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.ExpositionListValue;
import com.example.museaapplication.Classes.Json.ExpositionsList;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.WorkValue;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIRequests {
    private static APIRequests _instance;
    public static APIRequests getInstance(){
        if (_instance == null) _instance = new APIRequests();
        return _instance;
    }


    public void getAllMuseums(Delegate function) {
        Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseums();
        call.enqueue(new Callback<MuseoValue>() {
            @Override
            public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                MuseoValue mymuseumList = response.body();
                Museo[] museums = mymuseumList.getMuseums();
                SingletonDataHolder.getInstance().setMuseums(museums);
                CacheExhibitions();
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
        Call<ExhibitionValue> call = RetrofitClient.getInstance().getMyApi().getExhibition(m.get_id(), id_exh);
        call.enqueue(new Callback<ExhibitionValue>() {
            @Override
            public void onResponse(Call<ExhibitionValue> call, Response<ExhibitionValue> response) {
                ExhibitionValue exh = response.body();
                m.addExhibition(exh.getExhibition());
                //CacheWorks(m, exh.getExhibition());
            }

            @Override
            public void onFailure(Call<ExhibitionValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void getExhibitionText(Museo m, String id_exh) {
        Retrofit retro = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api api = retro.create(Api.class);
        Call<String> call = api.getExhibitionText(m.get_id(), id_exh);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("ExhibitionsText", "" + response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    void getWork(Exhibition e, String idMuseo , String idObra){
        Call<WorkValue> call = RetrofitClient.getInstance().getMyApi().getWork(idMuseo,e.get_id(), idObra);
        call.enqueue(new Callback<WorkValue>() {
            @Override
            public void onResponse(Call<WorkValue> call, Response<WorkValue> response) {
                WorkValue wv = response.body();
                Log.d("obra", wv.getWork().getAuthor());
                //e.addWork(wv.getWork());
            }

            @Override
            public void onFailure(Call<WorkValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());

                t.printStackTrace();
            }
        });
    }

    public void getExpositionsOfMuseums(Museo m){
        Call<ExpositionListValue> call = RetrofitClient.getInstance().getMyApi().getExpositions(m.get_id());
        call.enqueue(new Callback<ExpositionListValue>() {
            @Override
            public void onResponse(Call<ExpositionListValue> call, Response<ExpositionListValue> response) {
                ExpositionListValue expoListVal = response.body();
                for(Exhibition e : expoListVal.getMuseum().getExhibitions()) {
                    if (e != null){
                        Log.d("Mus", m.getName());
                        m.addExhibition(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ExpositionListValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());

                t.printStackTrace();
            }
        });
    }

    private void CacheExhibitions() {
        Museo[] museums = SingletonDataHolder.getInstance().getMuseums();
        for (Museo m: museums){
            m.setExhibitionObjects(new ArrayList<>());
            for(String s : m.getExhibitions()){
                if (!s.equals("")) {
                    getExhibitions(m, s);
                }
            }

        }
    }
    private void CacheWorks(Museo m, Exhibition e){
        for (String s : e.getWorks()){
            getWork(e, m.get_id(), s);
        }
    }
}
