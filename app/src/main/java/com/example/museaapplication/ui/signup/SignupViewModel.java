package com.example.museaapplication.ui.signup;


import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Dominio.User;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<Integer> res;

    public SignupViewModel() {
        res = new MutableLiveData<Integer>();
        res.setValue(0);
    }
    public MutableLiveData<Integer> getRes() {
        return res;
    }

    public boolean isEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        String UpperCaseRegex= ".*[A-Z].*";
        String LowerCaseRegex= ".*[a-z].*";
        String NumberRegex= ".*[0-9].*";
        //System.out.println(password.matches(NumberRegex) && password.matches(UpperCaseRegex) && password.matches(LowerCaseRegex));
        return password.matches(NumberRegex) && password.matches(UpperCaseRegex) && password.matches(LowerCaseRegex);

    }

    public void newSignup(String username, String password, String email) {
        User newUser = new User(username,password,email);
        Call<Void> call = RetrofitClient.getInstance().getMyApi().createUser(newUser);
        res.setValue(0);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.d("toString",response.toString());
                Log.d("code","" + response.code());

                if (response.code() == 200) {
                    Log.d("Respuesta","Usuario creado!");
                    res.setValue(1);
                }
                else if (response.code() == 404) {
                    Log.d("Respuesta","Usuario ya existe");
                    res.setValue(2);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
                res.setValue(-1);
            }


        });


    }


    public void setRes(MutableLiveData<Integer> res) {
        this.res = res;
    }
}
