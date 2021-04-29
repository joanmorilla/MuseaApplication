package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Location;
import com.example.museaapplication.Classes.Dominio.Restriction;
import com.google.gson.annotations.SerializedName;

public class ExpositionsList {
    @SerializedName("expositions")
    private Exhibition[] exhibitions;
    private Restriction[] restrictions;

    public Exhibition[] getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(Exhibition[] exhibitions) {
        this.exhibitions = exhibitions;
    }

    public Restriction[] getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restriction[] restrictions) {
        this.restrictions = restrictions;
    }
}
