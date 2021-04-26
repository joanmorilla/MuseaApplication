package com.example.museaapplication.ui.favourite;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Favourites;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.FavouritesValue;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteMusViewModel extends ViewModel{

        private MutableLiveData<Favourites[]> Favourites;

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
                    Favourites[] favourites = myfavlist.getFavouritesList();
                    Favourites.postValue(favourites);
                }

                @Override
                public void onFailure(Call<FavouritesValue> call, Throwable t) {
                    Log.e("TAG1", t.getLocalizedMessage());
                    Log.e("TAG2", t.getMessage());
                    t.printStackTrace();
                }
            });

        }

    }

