package com.example.museaapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.museaapplication.R;
import com.example.museaapplication.ui.login.LoginFragment;

import java.util.Locale;

public class InitialActivity extends AppCompatActivity {

    public static String curLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        getSupportFragmentManager().beginTransaction().add(R.id.InitialActivity,new LoginFragment()).show(new LoginFragment()).commit();
        loadSettings();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //curLanguage = newConfig.locale.getLanguage();
    }

    void loadSettings(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("MyLanguage", Locale.getDefault().getLanguage());
        curLanguage = lang;
        setLocale(lang);
    }

    void setLocale(String lName){
        Locale locale = new Locale(lName);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("MyLanguage", lName);
        editor.apply();
    }
}