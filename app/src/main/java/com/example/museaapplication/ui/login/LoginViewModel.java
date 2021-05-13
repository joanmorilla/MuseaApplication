package com.example.museaapplication.ui.login;

import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Json.UserValue;
import com.example.museaapplication.Classes.RetrofitClient;

import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Integer> nAttempts;
    private MutableLiveData<Integer> res;
    private int unlockTime;
    private boolean enabled;

    public LoginViewModel() {
        unlockTime = currentTime();
        enabled = true;
        nAttempts = new MutableLiveData<>();
        nAttempts.setValue(0);
        res = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getnAttempts() {
        return nAttempts;
    }
    public MutableLiveData<Integer> getRes() { return res; }

    public boolean validUsernamePassword(String username, String password) {

        nAttempts.setValue(nAttempts.getValue()+1);

        // Prevenir más loggeos hasta que pasen N segundos
        if (!enabled) {
            if (currentTime() > unlockTime) {
                enabled = true;
                nAttempts.setValue(0);
            } else
                return false;
        }

        if (username.isEmpty() || password.isEmpty()) return false;

        String base = "mobile" + ":" + "mobilePass";
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "password")
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build();

        res.setValue(0);
        Call<UserValue> call = RetrofitClient.getInstance().getMyApi().getUserAuth(authHeader,requestBody);

        call.enqueue(new Callback<UserValue>() {
            @Override
            public void onResponse(Call<UserValue> call, Response<UserValue> response) {

                Log.d("toString",response.toString());
                Log.d("code","" + response.code());

                if (response.code() == 200) {
                    Log.d("Respuesta","Usuario existe!");
                    res.setValue(1);
                }
                else if (response.code() == 400) {
                    Log.d("Respuesta","Usuario no existe");
                    res.setValue(2);
                }

            }

            @Override
            public void onFailure(Call<UserValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
                res.setValue(-1);
            }


        });

        return true;

    }

    public void disableLogin() {
        if (enabled) {
            enabled = false;
            unlockTime = currentTime() + 30;
        }
    }

    private int currentTime() { return (int) (Calendar.getInstance().getTime().getTime()/1000);}

    public int getRemainingTime() { return (unlockTime - currentTime()); }

    public void createUserInfo(String username, String email) {
        Call<Void> call2 = RetrofitClient.getInstance().getMyApi().createNewUser(username, email);
        call2.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call2, Response<Void> response2) {
                if (response2.code() == 200) {
                    Log.d("Respuesta 2 ","Usuario creado satisfactoriamente!");
                    res.setValue(1);
                }
                else if (response2.code() == 500) {
                    Log.d("Respuesta 2 ","No se ha podido crear el Usuario");
                    res.setValue(2);
                }
            }

            @Override
            public void onFailure(Call<Void> call2, Throwable t) {
                Log.e("TAG3", t.getLocalizedMessage());
                Log.e("TAG4", t.getMessage());
                t.printStackTrace();
                res.setValue(-1);
            }
        });
    }

}
