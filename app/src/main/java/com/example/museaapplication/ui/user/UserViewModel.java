package com.example.museaapplication.ui.user;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Favourites;
import com.example.museaapplication.Classes.Dominio.Likes;
import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.Json.FavouritesValue;
import com.example.museaapplication.Classes.Json.LikesValue;
import com.example.museaapplication.Classes.Json.UserInfoValue;
import com.example.museaapplication.Classes.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private MutableLiveData<UserInfo> Userinfo;
    private MutableLiveData<Favourites[]> Favourites;
    private MutableLiveData<Likes[]> Likes;

    public UserViewModel() {
    }

    public LiveData<Likes[]> getLikes()
    {
        if (Likes == null) {
            Likes = new MutableLiveData<Likes[]>();
            loadlikes();
        }
        return Likes;

    }

    public LiveData<Likes[]> UpdateLikes()
    {
        if (Likes == null) {
            Likes = new MutableLiveData<Likes[]>();
            loadlikes();
        }
        return Likes;

    }


    public void loadlikes() {
        Call<LikesValue> call = RetrofitClient.getInstance().getMyApi().getLikes();
        call.enqueue(new Callback<LikesValue>() {
            @Override
            public void onResponse(Call<LikesValue> call, Response<LikesValue> response) {
                LikesValue mylikelist = response.body();
                Likes[] likes = mylikelist.getLikesList();
                Likes.postValue(likes);
            }

            @Override
            public void onFailure(Call<LikesValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });

    }

    public LiveData<Favourites[]> getFavourites()
    {
        if (Favourites == null) {
            Favourites = new MutableLiveData<Favourites[]>();
            loadUsers();
        }
        return Favourites;

    }

    public void loadUsers() {
        Call<FavouritesValue> call = RetrofitClient.getInstance().getMyApi().getFavourites();
        call.enqueue(new Callback<FavouritesValue>() {
            @Override
            public void onResponse(Call<FavouritesValue> call, Response<FavouritesValue> response) {
                FavouritesValue myfavlist = response.body();
                /*Favourites[] favourites = myfavlist.getFavouritesList();
                Favourites.postValue(favourites);*/
            }

            @Override
            public void onFailure(Call<FavouritesValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });

    }

    public MutableLiveData<UserInfo> getinfoUser() {
        if (Userinfo == null){
            Userinfo = new MutableLiveData<UserInfo>();
            loadUsersinfo();
        }
        return Userinfo;
    }

    public void loadUsersinfo() {
        Call<UserInfoValue> call = RetrofitClient.getInstance().getMyApi().getUserInfo();
        call.enqueue(new Callback<UserInfoValue>() {

            @Override
            public void onResponse(Call<UserInfoValue> call, Response<UserInfoValue> response) {
                UserInfoValue myuserinfo = response.body();
                UserInfo userin = myuserinfo.getUserinfo();
                Userinfo.setValue(userin);
            }

            @Override
            public void onFailure(Call<UserInfoValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
            });
        }

}
