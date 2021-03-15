package com.example.museaapplication.Classes;

import com.example.museaapplication.Classes.Json.ExhibitionValue;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.WorkValue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://musea-api.herokuapp.com/";
    @GET("museums")
    Call<MuseoValue> getMuseums(@Query("name") String n);
    @GET("museums/{idMuseo}/{idExpo}")
    Call<ExhibitionValue> getExhibition(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId);
    @GET("museums/{idMuseo}/{idExpo}")
    Call<String> getExhibitionText(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId);

    @GET("museums/{idMuseo}/{idExpo}/{idObra}")
    Call<WorkValue> getWork(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId, @Path("idObra") String idObra);

}
