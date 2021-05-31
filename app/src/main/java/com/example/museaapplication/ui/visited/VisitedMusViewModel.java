package com.example.museaapplication.ui.visited;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Dominio.Visited;
import com.example.museaapplication.Classes.Json.AuxMuseo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.VisitedValue;
import com.example.museaapplication.Classes.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitedMusViewModel extends ViewModel {

    private MutableLiveData<Visited[]> Visiteds;

    private MutableLiveData<String> museo;

    private MutableLiveData<ArrayList<Museo>> museos;

    public void addVisitedMuseum(String id_museum) {
        Call<Void> call = RetrofitClient.getInstance().getMyApi().addVisitedMuseum("raulfersan5",id_museum);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.d("POSTTTTTT",response.toString());
                Log.d("POSTTTTTT","" + response.code());

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();

            }


        });
    }

    public MutableLiveData<Visited[]> getVisited() {
        if (Visiteds == null){
            Visiteds = new MutableLiveData<Visited[]>();
            loadVisited();
        }
        return Visiteds;
    }

    public void loadVisited(){
        Call<VisitedValue> call = RetrofitClient.getInstance().getMyApi().getVisitedMuseum("raulfersan5");
        call.enqueue(new Callback<VisitedValue>() {
            @Override
            public void onResponse(Call<VisitedValue> call, Response<VisitedValue> response) {
                Log.d("OYEEEEEEEE",response.toString());
                Log.d("OYEEEEEEEE","" + response.code());
                VisitedValue myvisitedlist = response.body();
                Visited[] visited = myvisitedlist.getVisitedList();
                Visiteds.postValue(visited);


            }

            @Override
            public void onFailure(Call<VisitedValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public MutableLiveData<String> getMuseobyid() {
        if (museo == null){
            museo = new MutableLiveData<String>();
        }
        Log.e("getmuseo", "Llega:");
        Log.e("getmuseo", String.valueOf(museo));
        return museo;
    }

    public void loadMuseum(String idMuseo){
        Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseum(idMuseo);
        Log.e("loadmuseo", idMuseo);
        call.enqueue(new Callback<MuseoValue>() {
            @Override
            public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                AuxMuseo aux = response.body().getMuseum();
                Museo museum = new Museo();
                museum.setName(aux.getName());
                Log.e("loadmuseo", "HOLAAA");
                String nameM = museum.getName();
                Log.e("loadmuseo", "Llega:");
                museo.postValue(nameM);
                Log.e("loadmuseo", nameM);
            }

            @Override
            public void onFailure(Call<MuseoValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public MutableLiveData<ArrayList<Museo>> getMuseos() {
        if (museos == null){
            museos = new MutableLiveData<ArrayList<Museo>>();
        }
        loadMuseums();
        return museos;
    }

    public void loadMuseums(){
        if(museos == null){
            museos = new MutableLiveData<ArrayList<Museo>>();
        }
        ArrayList<Museo> museoslist = new ArrayList<Museo>();
        Log.e("FORVISITED", String.valueOf(Visiteds.getValue().length));
        for(int i=0; i< Visiteds.getValue().length; ++i){
            Visited[] vis = Visiteds.getValue();
            Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseum(vis[i].museumId());
            call.enqueue(new Callback<MuseoValue>() {
                @Override
                public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                    AuxMuseo aux = response.body().getMuseum();
                    Museo museum = new Museo();
                    museum.setName(aux.getName());
                    Log.e("NAMEMUSEO", aux.getName());
                    museum.setImage(aux.getImage());
                    museoslist.add(museum);
                    Log.e("FORVISITED", String.valueOf(museoslist.size()));
                    museos.postValue(museoslist);
                }

                @Override
                public void onFailure(Call<MuseoValue> call, Throwable t) {
                    Log.e("TAG1", t.getLocalizedMessage());
                    Log.e("TAG2", t.getMessage());
                    t.printStackTrace();
                }
            });

        }


    }

}