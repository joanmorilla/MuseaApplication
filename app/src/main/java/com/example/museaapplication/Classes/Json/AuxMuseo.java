package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Descriptions;
import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Restriction;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuxMuseo implements Serializable {
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

    private Restriction[] restrictions;

    public Exhibition[] getExhibitions() {
        return exhibitions;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Descriptions getDescriptions() {
        return descriptions;
    }

    public String getImage() {
        return image;
    }

    public Restriction[] getRestrictions() {
        return restrictions;
    }
}
