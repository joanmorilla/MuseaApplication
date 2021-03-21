package com.example.museaapplication.Classes.Json;

import java.io.Serializable;

public class Descriptions implements Serializable {
        private String ca;
        private String es;
        private String en;


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
