package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Location;
import com.google.gson.annotations.SerializedName;

public class ExpositionsList {
    @SerializedName("expositions")
    private Exhibition[] exhibitions;

    public Exhibition[] getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(Exhibition[] exhibitions) {
        this.exhibitions = exhibitions;
    }
}
