package com.example.museaapplication.Classes;

import com.example.museaapplication.Classes.Dominio.User;
import com.example.museaapplication.Classes.Json.ExhibitionValue;
import com.example.museaapplication.Classes.Json.ExpositionListValue;
import com.example.museaapplication.Classes.Json.ExpositionsList;
import com.example.museaapplication.Classes.Json.LikesValue;
import com.example.museaapplication.Classes.Json.InfoValue;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.UserInfoValue;
import com.example.museaapplication.Classes.Json.UserValue;
import com.example.museaapplication.Classes.Json.VisitedValue;
import com.example.museaapplication.Classes.Json.WorkValue;
import com.example.museaapplication.Classes.Json.WorksArray;
import com.example.museaapplication.Classes.Json.WorksValue;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://aplication-api.herokuapp.com/";

    // Museums, exhibitions and works
    @GET("museums")
    Call<MuseoValue> getMuseums();

    @GET("https://musea-api.herokuapp.com/museums/{museumId}")
    Call<MuseoValue> getMuseumbyid(@Path("museumId") String museumId);

    @GET("museums/{idMuseo}/{idExpo}")
    Call<WorksValue> getExhibition(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId);

    @GET("museums/{idMuseo}")
    Call<ExpositionListValue> getExpositions(@Path("idMuseo") String idMuseo);

    @GET("museums/{idMuseo}/{idExpo}")
    Call<String> getExhibitionText(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId);

    @GET("museums/{idMuseo}/{idExpo}/{idObra}")
    Call<WorkValue> getWork(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId, @Path("idObra") String idObra);

    @GET("https://musea-api.herokuapp.com/users/RaulPes")
    Call<UserInfoValue> getUserInfo();


    @GET("https://musea-api.herokuapp.com/users/RaulPes/likes")
    Call<LikesValue> getLikes();

    @PUT("https://musea-api.herokuapp.com/users/{userId}?")
    Call<Void> addInfoUser(@Path("userId") String userId, @Query("name") String nameuser, @Query("bio") String userbio);

    @POST("https://musea-api.herokuapp.com/users/{username}/visited?")
    Call<Void> addVisitedMuseum(@Path("username") String username, @Query("museum") String museum);

    @GET("https://musea-api.herokuapp.com/users/{username}/visited")
    Call<VisitedValue> getVisitedMuseum(@Path("username") String username);

    // User favorites
    @POST("https://musea-api.herokuapp.com/users/{username}/likes?")
    Call<Void> likeWork(@Path("username") String userId, @Query("artwork") String artworkId);

    // Info of schedules
    @GET("https://musea-api.herokuapp.com/info?")
    Call<InfoValue> getInfo(@Query("name") String nameMuseo, @Query("city") String cityMuseo);


    // User authentication
    @POST("https://musea-authorization-server.herokuapp.com/signup")
    Call<Void> createUserAuth(@Body User user);

    @POST("https://musea-authorization-server.herokuapp.com/oauth/token")
    Call<UserValue> getUserAuth(@Header("Authorization") String authHeader,
                                @Body RequestBody body);

    @POST("https://musea-api.herokuapp.com/users")
    Call<Void> createNewUser(@Query("username") String username,
                             @Query("email") String email);

    @GET("https://museaimages.s3.eu-west-3.amazonaws.com/logo.png")
    Call<User> getImage();
}
