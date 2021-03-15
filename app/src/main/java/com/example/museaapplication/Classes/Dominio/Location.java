package com.example.museaapplication.Classes.Dominio;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {
    @SerializedName("$numberDecimal")
    private float numberDecimal;

    public float getNumberDecimal() {
        return numberDecimal;
    }

    public void setNumberDecimal(float numberDecimal) {
        this.numberDecimal = numberDecimal;
    }
}
