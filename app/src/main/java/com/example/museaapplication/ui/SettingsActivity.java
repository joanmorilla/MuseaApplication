package com.example.museaapplication.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.museaapplication.InitialActivity;
import com.example.museaapplication.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    Button idiomaSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        idiomaSettings = findViewById(R.id.btn_idioma);
        idiomaSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });
    }


    void showChangeLanguageDialog() {
        String[] languages = {"Español", "Català", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        mBuilder.setTitle(R.string.language_dialog_title);
        mBuilder.setSingleChoiceItems(languages, checkPos(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), InitialActivity.class);
                if (checkPos() != i) {
                    if (i == 0) {
                        setLocale("es");
                        finish();
                        startActivity(intent);
                    } else if (i == 1) {
                        setLocale("ca");
                        finish();
                        startActivity(intent);
                    } else {
                        setLocale("en");
                        finish();
                        startActivity(intent);
                    }
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    int checkPos(){
        switch(InitialActivity.curLanguage){
            case "en":
                return 2;
            case "es":
                return 0;
            case "ca":
                return 1;
        }
        return -1;
    }

    void setLocale(String lName){
        Locale locale = new Locale(lName);
        Locale.setDefault(locale);
        Resources res = getApplicationContext().getResources();
        Configuration config = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }else config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
        InitialActivity.curLanguage = lName;

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("MyLanguage", lName);
        editor.apply();
    }
}