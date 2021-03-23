package com.example.museaapplication.Classes;

import com.example.museaapplication.Classes.Dominio.User;
import com.example.museaapplication.Classes.Json.ExhibitionValue;
import com.example.museaapplication.Classes.Json.ExpositionListValue;
import com.example.museaapplication.Classes.Json.ExpositionsList;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.UserValue;
import com.example.museaapplication.Classes.Json.WorkValue;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://aplication-api.herokuapp.com/";
    @GET("museums")
    Call<MuseoValue> getMuseums();

    @GET("museums/{idMuseo}/{idExpo}")
    Call<ExhibitionValue> getExhibition(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId);

    @GET("museums/{idMuseo}")
    Call<ExpositionListValue> getExpositions(@Path("idMuseo") String idMuseo);

    @GET("museums/{idMuseo}/{idExpo}")
    Call<String> getExhibitionText(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId);

    @GET("museums/{idMuseo}/{idExpo}/{idObra}")
    Call<WorkValue> getWork(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId, @Path("idObra") String idObra);

    // User authentication
    @POST("https://musea-authorization-server.herokuapp.com/signup")
    Call<Void> createUser(@Body User user);

    @POST("https://musea-authorization-server.herokuapp.com/oauth/token")
    Call<UserValue> getUser(@Header("Authorization") String authHeader,
                            @Body RequestBody body);
}
