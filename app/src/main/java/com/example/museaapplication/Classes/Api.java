package com.example.museaapplication.Classes;

import com.example.museaapplication.Classes.Dominio.Museo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "https://musea-api.herokuapp.com/";
    @GET("museums")
    Call<List<Museo>> getMuseums();
}
