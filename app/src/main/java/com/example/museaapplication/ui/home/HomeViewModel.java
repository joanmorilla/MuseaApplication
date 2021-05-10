package com.example.museaapplication.ui.home;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Dominio.Likes;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.LikesValue;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Museo[]> Museums;
    private MutableLiveData<Museo[]> FavouriteMuseums = new MutableLiveData<>();
    private Stack<Integer> order = new Stack<>();

    private ArrayList<Museo> curFavorites;

    private Likes[] favourites;
    //private MutableLiveData<String[]> horaris;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Propers");
    }

    public LiveData<Museo[]> getFavouriteMuseums() {
        if (FavouriteMuseums == null) {
            FavouriteMuseums = new MutableLiveData<>();
        }
        return FavouriteMuseums;
    }

    public LiveData<Museo[]> getMuseums() {
        if (Museums == null){
            Museums = new MutableLiveData<>();
            loadUsers();
        }
        return Museums;
    }

    public LiveData<Museo[]> getMuseumsInMap() {
        return Museums;
    }

    public void loadUsers() {
        Call<LikesValue> callFavourites = RetrofitClient.getInstance().getMyApi().getFavMuseums("RaulPes");
        callFavourites.enqueue(new Callback<LikesValue>() {
            @Override
            public void onResponse(Call<LikesValue> call, Response<LikesValue> response) {
                favourites = response.body().getLikesList();
                Call<MuseoValue> callMuseums = RetrofitClient.getInstance().getMyApi().getMuseums();
                callMuseums.enqueue(new Callback<MuseoValue>() {
                    @Override
                    public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                        MuseoValue mymuseumList = response.body();
                        Museo[] museums = mymuseumList.getMuseums();
                        cacheExpositionsAndInfo(museums);
                        cacheFavMuseums(museums);
                    }

                    @Override
                    public void onFailure(Call<MuseoValue> call, Throwable t) {
                        Log.e("TAG1Museo", t.getLocalizedMessage());
                        Log.e("TAG2Museo", t.getMessage());
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

            @Override
            public void onFailure(Call<LikesValue> call, Throwable t) {

            }
        });

    }

    private void cacheFavMuseums(Museo[] museums){
        if (curFavorites == null) curFavorites = new ArrayList<>();
        for (Museo m : museums){
            boolean res = checkFavourites(m.get_id());
            if (res) {
                Log.e("Funca",m.getName());
                curFavorites.add(m);
            }
            m.setLiked(res);
        }
        FavouriteMuseums.postValue(curFavorites.toArray(new Museo[0]));
    }

    private boolean checkFavourites(String id){
        for (Likes l : favourites){
            if (l.getArtworkId().equals(id)) return true;
        }
        return false;
    }

    private void cacheExpositionsAndInfo(Museo[] museums) {
        Call<LikesValue> call = RetrofitClient.getInstance().getMyApi().getLikes();
        call.enqueue(new Callback<LikesValue>() {
            @Override
            public void onResponse(Call<LikesValue> call, Response<LikesValue> response) {
                APIRequests.getInstance().likes = response.body() != null ? response.body().getLikesList() : new Likes[0];
                int i = 0;
                for(Museo m: museums){
                    order.add(i);
                    APIRequests.getInstance().getExpositionsOfMuseum(m);
                    APIRequests.getInstance().getInfo(museums, i, order, Museums);
                    i++;
                }
            }

            @Override
            public void onFailure(Call<LikesValue> call, Throwable t) {

            }
        });
    }

    public LiveData<String> getText() {
        return mText;
    }
}