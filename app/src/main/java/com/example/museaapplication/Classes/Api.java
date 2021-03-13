package com.example.museaapplication.Classes;

import com.example.museaapplication.Classes.Json.MuseoValue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://musea-api.herokuapp.com/";
    @GET("museums")
    Call<MuseoValue> getMuseums(@Query("name") String n);
}
