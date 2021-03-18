package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Location;
import com.google.gson.annotations.SerializedName;

public class ExpositionsList {
    private Location[] location;
    @SerializedName("expositions")
    private Exhibition[] exhibitions;
    private String _id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Descriptions descriptions;
    @SerializedName("image")
    private String image;

    public Exhibition[] getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(Exhibition[] exhibitions) {
        this.exhibitions = exhibitions;
    }
}
