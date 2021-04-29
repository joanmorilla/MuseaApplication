package com.example.museaapplication.ui.signup;


import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.User;
import com.example.museaapplication.Classes.RetrofitClient;

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
        Call<Void> call = RetrofitClient.getInstance().getMyApi().createUserAuth(newUser);
        res.setValue(0);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.d("toString",response.toString());
                Log.d("code","" + response.code());

                if (response.code() == 200) {
                    Log.d("Respuesta","Auth Usuario creado!");
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
                else if (response.code() == 400) {
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
