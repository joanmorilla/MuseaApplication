package com.example.museaapplication.Classes.Dominio;

import com.example.museaapplication.ui.InitialActivity;

import java.io.Serializable;

public class Descriptions implements Serializable {
        private String ca;
        private String es;
        private String en;

    public String getText() {
        String languagename = InitialActivity.curLanguage;
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
