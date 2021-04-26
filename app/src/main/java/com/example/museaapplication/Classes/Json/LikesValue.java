package com.example.museaapplication.Classes.Json;


import com.example.museaapplication.Classes.Dominio.Likes;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LikesValue implements Serializable {
    @SerializedName("likes")
    private Likes[] likes;

    public Likes getLikes(int i) {
        return likes[i];
    }


    public Likes[] getLikesList(){return likes;}

}
