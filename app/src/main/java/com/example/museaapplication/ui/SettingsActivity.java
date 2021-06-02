package com.example.museaapplication.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.museaapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    Button idiomaSettings;
    Button logout;
    Button ayudaButton;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        idiomaSettings = findViewById(R.id.btn_idioma);
        logout = findViewById(R.id.et_cerrar_ses);
        idiomaSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteSharedPreferences();
                signOut();
                Intent intent = new Intent(SettingsActivity.this, InitialActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
       ayudaButton = findViewById(R.id.btn_ajuda);
       ayudaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp();
            }
        });
    }

    private void DeleteSharedPreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("Settings",0);
        sharedPref.edit().clear().commit();
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

    void showHelp() {
        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}