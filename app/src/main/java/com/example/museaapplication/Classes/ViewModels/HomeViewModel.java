package com.example.museaapplication.Classes.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Museo[]> Museums;

    public LiveData<Museo[]> getMuseums() {
        if (Museums == null){
            Museums = new MutableLiveData<Museo[]>();
        }
        return Museums;
    }

    public void loadUsers(){
        Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseums();
        call.enqueue(new Callback<MuseoValue>() {
            @Override
            public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                MuseoValue mymuseumList = response.body();
                Museo[] museums = mymuseumList.getMuseums();
                Museums.postValue(museums);
            }

            @Override
            public void onFailure(Call<MuseoValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
                SingletonDataHolder.getInstance().setMuseums(null);
            }
        });
    }
}
