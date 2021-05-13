package com.example.museaapplication.ui.edit;


import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.User;
import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class edit_userViewModel extends ViewModel {

    private MutableLiveData<UserInfo> Userinfo;
    private MutableLiveData<String> finish;

    public edit_userViewModel() {
    }

    public LiveData<String> getFinish() {
        if (finish == null) finish = new MutableLiveData<>();
        return finish;
    }

    public void edit_user_info(String name, String bio){
        Log.d("Respuesta",name);
        Log.d("Respuesta",bio);
        //"60903ae74bba1e0015582853"
        Call<Void> call = RetrofitClient.getInstance().getMyApi().addInfoUser("RaulPes",name,bio);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("toString",response.toString());
                Log.d("code","" + response.code());

                if (response.code() == 200) {
                    finish.postValue("OK");
                    SingletonDataHolder.userViewModel.loadUsersinfo();
                    Log.d("Respuesta","Usuario existe!");

                }
                else if (response.code() == 404) {
                    finish.postValue("ERROR");
                    Log.d("Respuesta","Usuario no existe");

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG3", t.getLocalizedMessage());
                Log.e("TAG4", t.getMessage());
                t.printStackTrace();
            }
        });
    }



}
