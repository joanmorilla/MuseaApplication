package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Favourites;
import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavouritesValue implements Serializable {
    @SerializedName("favourites")
    private Favourites[] favourites;

    public Favourites getFavourites(int i) {
        return favourites[i];
    }


    public Favourites[] getFavouritesList(){return favourites;}


}
