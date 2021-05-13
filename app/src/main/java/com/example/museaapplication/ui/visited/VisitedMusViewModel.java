package com.example.museaapplication.ui.visited;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Dominio.Visited;
import com.example.museaapplication.Classes.Dominio.User;
import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.VisitedValue;
import com.example.museaapplication.Classes.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitedMusViewModel extends ViewModel {

    private MutableLiveData<Visited[]> Visiteds;

    private MutableLiveData<Museo[]> museo;

    public void addVisitedMuseum(String id_museum) {
        Call<Void> call = RetrofitClient.getInstance().getMyApi().addVisitedMuseum("RaulPes",id_museum);
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
        Call<VisitedValue> call = RetrofitClient.getInstance().getMyApi().getVisitedMuseum("RaulPes");
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

    public MutableLiveData<Museo[]> getMuseobyid(String idMuseum) {
        if (museo == null){
            museo = new MutableLiveData<Museo[]>();
        }
        return museo;

    }

}