package com.example.museaapplication.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Likes;
import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.Json.LikesValue;
import com.example.museaapplication.Classes.Json.UserInfoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;

import com.example.museaapplication.R;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private MutableLiveData<UserInfo> Userinfo;
    private MutableLiveData<Likes[]> Likes;
    private MutableLiveData<String> finishBuy;

    public UserViewModel() {
    }

    public LiveData<Likes[]> getLikes()
    {
        Likes = new MutableLiveData<Likes[]>();
        loadlikes();

        return Likes;

    }


    public void UpdateLikes() {
        Likes = new MutableLiveData<Likes[]>();
        loadlikes();
    }
    public void loadlikes() {
        Log.e("Entra", "aaa");
        Call<LikesValue> call = RetrofitClient.getInstance().getMyApi().getLikes(SingletonDataHolder.getInstance().getLoggedUser().getUserId());
        call.enqueue(new Callback<LikesValue>() {
            @Override
            public void onResponse(Call<LikesValue> call, Response<LikesValue> response) {
                LikesValue mylikelist = response.body();
                Likes[] likes = mylikelist.getLikesList();
                Likes.postValue(likes);
                Log.d("Load Likes", "response = " + response.code());
            }

            @Override
            public void onFailure(Call<LikesValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });

    }

    public MutableLiveData<UserInfo> getinfoUser() {
        if (Userinfo == null){
            Userinfo = new MutableLiveData<>();
            loadUsersinfo();
        }
        return Userinfo;
    }

    public void loadUsersinfo() {
        String loogedUserEmail = SingletonDataHolder.getInstance().getLoggedUser().getEmail();
        Call<UserInfoValue> call = RetrofitClient.getInstance().getMyApi().getUserInfo(loogedUserEmail);
        call.enqueue(new Callback<UserInfoValue>() {

            @Override
            public void onResponse(Call<UserInfoValue> call, Response<UserInfoValue> response) {
                UserInfoValue myuserinfo = response.body();

                if (myuserinfo != null) {
                    UserInfo userin = myuserinfo.getUserinfo();
                    Userinfo.setValue(userin);
                    SingletonDataHolder.getInstance().setLoggedUser(userin);
                }
            }

            @Override
            public void onFailure(Call<UserInfoValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
            });
        }

    public void UpdateUserInfo()
    {
        Likes = new MutableLiveData<Likes[]>();
        Log.e("Funciona", "UPDATELIKES VIEWMODEL");
        loadUsersinfo();
    }

    public boolean IsPremium() {
        return SingletonDataHolder.getInstance().getLoggedUser().isPremium();
    }

    public void addPremiumMembership(int days) {
        Call<Void> call = RetrofitClient.getInstance().getMyApi().addPremiumMembership(getinfoUser().getValue().getUserId(),String.valueOf(days));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("toString",response.toString());
                Log.d("code","" + response.code());

                if (response.code() == 200) {
                    finishBuy.postValue("OK");
                    loadUsersinfo();
                    Log.d("Respuesta", "Se ha añadido membership al usuario");

                }
                else if (response.code() == 404) {
                    finishBuy.postValue("NOT_OK");
                    loadUsersinfo();
                    Log.d("Respuesta","No se ha podido añadir membership");

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

    public void spendPoints(int points) {
        Call<Void> call = RetrofitClient.getInstance().getMyApi().spendPoints(getinfoUser().getValue().getUserId(),String.valueOf(points));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Log.d("Respuesta", "El usuario ha gastado puntos");

                }
                else if (response.code() == 404) {

                    Log.d("Respuesta","El usuario no ha podido gastar puntos");

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

    public LiveData<String> getFinishBuy() {
        if (finishBuy == null) {
            finishBuy = new MutableLiveData<>();
            finishBuy.postValue("none");
        }
        return finishBuy;
    }

    public void resetFinishBuy() {
        finishBuy = new MutableLiveData<>();
        finishBuy.postValue("none");

    }

}
