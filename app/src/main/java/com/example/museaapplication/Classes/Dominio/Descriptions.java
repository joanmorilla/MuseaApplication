package com.example.museaapplication.Classes.Dominio;

import android.util.Log;

import java.io.Serializable;
import java.util.Locale;

public class Descriptions implements Serializable {
        private String ca;
        private String es;
        private String en;

    public String getText() {
        String languagename = Locale.getDefault().getDisplayLanguage();
        Log.d("Lenguaje", languagename);
        switch (languagename) {
            case "català":
                return ca;
            case "español":
                return es;
            default:
                return en;
        }
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
