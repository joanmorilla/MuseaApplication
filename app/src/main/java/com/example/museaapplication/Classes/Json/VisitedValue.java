package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.Dominio.Visited;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VisitedValue implements Serializable {
    @SerializedName("visited")
    private Visited[] visited;

    public Visited getFavourites(int i) {
        return visited[i];
    }


    public Visited[] getVisitedList(){return visited;}


}
