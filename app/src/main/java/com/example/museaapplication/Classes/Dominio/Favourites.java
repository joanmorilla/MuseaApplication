package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;

public class Favourites implements Serializable {

    private String museumId;
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
