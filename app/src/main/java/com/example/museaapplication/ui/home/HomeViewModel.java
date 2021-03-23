package com.example.museaapplication.ui.home;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Museo[]> Museums;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Propers");
    }

    public LiveData<Museo[]> getMuseums() {
        if (Museums == null){
            Museums = new MutableLiveData<Museo[]>();
            loadUsers();
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
                cacheExpositions(museums);
            }

            @Override
            public void onFailure(Call<MuseoValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
                /*new CountDownTimer(1000, 100){
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        loadUsers();
                    }
                };*/
            }
        });
    }
    private void cacheExpositions(Museo[] museums) {
        for(Museo m: museums){
            APIRequests.getInstance().getExpositionsOfMuseums(m);
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}