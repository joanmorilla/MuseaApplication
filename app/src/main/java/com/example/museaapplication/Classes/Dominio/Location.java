package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;

public class Location implements Serializable {
    private float numberDecimal;

    public float getNumberDecimal() {
        return numberDecimal;
    }

    public void setNumberDecimal(float numberDecimal) {
        this.numberDecimal = numberDecimal;
    }
}
