package com.example.museaapplication.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Dominio.Likes;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.LikesValue;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<Museo[]> Museums;
    private MutableLiveData<Museo[]> FavouriteMuseums = new MutableLiveData<>();
    private MutableLiveData<Marker> curPosMarker = new MutableLiveData<>();
    private final Stack<Integer> order = new Stack<>();

    private ArrayList<Museo> curFavorites;

    private Likes[] favourites;
    //private MutableLiveData<String[]> horaris;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Propers");
    }

    public LiveData<Marker> getCurPosMarker() {
        if (curPosMarker == null) curPosMarker = new MutableLiveData<>();
        return curPosMarker;
    }
    public Marker getMarkerValue() {
        if (curPosMarker != null)
            return curPosMarker.getValue();
        return null;
    }
    public void setCurMarker(Marker m) {
        if (curPosMarker == null) curPosMarker = new MutableLiveData<>();
        curPosMarker.postValue(m);
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
    public void unlikeMuseum(String _id) {
        Call<Void> call = RetrofitClient.getInstance().getMyApi().favMuseum("RaulPes", _id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
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
    public void newFavourite(Museo m){
        if (curFavorites != null) {
            curFavorites.remove(m);
        } else curFavorites = new ArrayList<>();
            curFavorites.add(0, m);
            FavouriteMuseums.postValue(curFavorites.toArray(new Museo[0]));
    }
    public void unFavorite(Museo m){
        if (curFavorites != null) {
            curFavorites.remove(m);
            FavouriteMuseums.postValue(curFavorites.toArray(new Museo[0]));
        }
    }

    private void cacheFavMuseums(Museo[] museums){
        if (curFavorites == null) curFavorites = new ArrayList<>();
        // Better solution
        for (int i = favourites.length-1; i >= 0; i--){
            checkMuseums(museums, favourites[i].getArtworkId());
        }

        // Otra idea seria guardar los ids en una arraylist de strings y consultar las posiciones de inserción dentro de la lista

       /* // Working alternative
        for (Museo m : museums){
            boolean res = checkFavourites(m);
            m.setLiked(res);
        }*/

        FavouriteMuseums.postValue(curFavorites.toArray(new Museo[0]));
    }
    private void checkMuseums(Museo[] museums, String id){
        for (Museo m : museums) {
            if (m.get_id().equals(id)) {
                m.setLiked(true);
                curFavorites.add(m);
                return;
            }
        }
        // Here we place the code for downloading the favourite museums we don't already have
    }

    private boolean checkFavourites(Museo m){
        if (curFavorites == null) curFavorites = new ArrayList<>();
        for (Likes l : favourites){
            if (l.getArtworkId().equals(m.get_id())) {
                curFavorites.add(m);
                return true;
            }
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