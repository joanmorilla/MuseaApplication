package com.example.museaapplication.Classes.Dominio;

import android.content.res.Resources;
import android.util.Log;

import com.example.museaapplication.InitialActivity;
import com.example.museaapplication.ui.MainActivity;

import java.io.Serializable;
import java.util.Locale;

public class Descriptions implements Serializable {
        private String ca;
        private String es;
        private String en;

    public String getText() {
        String languagename = InitialActivity.curLanguage;
        Log.d("Lenguaje", languagename);
        switch (languagename) {
            case "ca":
                return ca;
            case "es":
                return es;
            default:
                return en;
        }
    }
}
