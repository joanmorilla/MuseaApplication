package com.example.museaapplication.Classes;

import com.example.museaapplication.Classes.Dominio.Comment;
import com.example.museaapplication.Classes.Dominio.User;
import com.example.museaapplication.Classes.Json.CommentsValue;
import com.example.museaapplication.Classes.Json.ExpositionListValue;
import com.example.museaapplication.Classes.Json.InfoValue;
import com.example.museaapplication.Classes.Json.LikesValue;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.QuizzValue;
import com.example.museaapplication.Classes.Json.UserInfoValue;
import com.example.museaapplication.Classes.Json.UserValue;
import com.example.museaapplication.Classes.Json.VisitedValue;
import com.example.museaapplication.Classes.Json.WorkValue;
import com.example.museaapplication.Classes.Json.WorksValue;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://aplication-api.herokuapp.com/";

    // Museums, exhibitions and works
    @GET("museums")
    Call<MuseoValue> getMuseums();

    @GET("https://musea-api.herokuapp.com/museums/{idMuseo}")
    Call<MuseoValue> getMuseum(@Path("idMuseo") String idMuseo);

    @GET("museums/{idMuseo}/{idExpo}")
    Call<WorksValue> getExhibition(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId);

    @GET("museums/{idMuseo}")
    Call<ExpositionListValue> getExpositions(@Path("idMuseo") String idMuseo);

    @GET("museums/{idMuseo}/{idExpo}")
    Call<String> getExhibitionText(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId);

    @GET("museums/{idMuseo}/{idExpo}/{idObra}")
    Call<WorkValue> getWork(@Path("idMuseo") String museumId, @Path("idExpo") String exhibitionId, @Path("idObra") String idObra);

    @GET("https://musea-api.herokuapp.com/users/{mailUser}")
    Call<UserInfoValue> getUserInfo(@Path("mailUser") String mailUser);

    // Comments
    @GET("https://musea-api.herokuapp.com/comments?")
    Call<CommentsValue> getComments(@Query("artworkId") String artworkId);

    @POST("https://musea-api.herokuapp.com/comments?")
    Call<Comment> postComment(@Query("artworkId") String artworkId, @Query("content") String content, @Query("author") String author);

    @DELETE("https://musea-api.herokuapp.com/comments/{commentId}")
    Call<Void> deleteComment(@Path("commentId") String commentId);

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

    @POST("https://musea-api.herokuapp.com/users/{userId}/favourites")
    Call<Void> favMuseum(@Path("userId") String userId, @Query("museum") String museumId);

    @GET ("https://musea-api.herokuapp.com/users/{userId}/favourites")
    Call<LikesValue> getFavMuseums(@Path("userId") String userId);

    // Info of schedules
    @GET("https://musea-api.herokuapp.com/info?")
    Call<InfoValue> getInfo(@Query("name") String nameMuseo, @Query("city") String cityMuseo);

    // User premium
    @PUT("https://musea-api.herokuapp.com/users/{username}/premium")
    Call<Void> addPremiumMembership(@Path("username") String username,@Query("days") String days);

    // User spend points
    @POST("https://musea-api.herokuapp.com/users/{username}/spend")
    Call<Void> spendPoints(@Path("username") String username,@Query("points") String points);

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

    // Quizzes
    @GET("https://musea-api.herokuapp.com/quizzes")
    Call<QuizzValue> getQuizzes();

    @POST("https://musea-api.herokuapp.com/users/{username}/points")
    Call<Void> updatePoints(@Path("username") String username,
                            @Query("points") String points,
                            @Query("total") String total);

    // Reports
    @POST("https://musea-api.herokuapp.com/reports")
    Call<Void> reportUser(@Query("informant") String idInformant, @Query("comment") String idComment);

}
