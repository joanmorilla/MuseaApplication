package com.example.museaapplication.Classes.Dominio;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Visited implements Serializable {
    @SerializedName("museumId")
    private String museumId;
    @SerializedName("image")
    private String image;


    public String museumId() {
        return museumId;
    }

    public void setmuseumId(String museumId) {
        this.museumId = museumId;
    }

    public String image() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }
}
