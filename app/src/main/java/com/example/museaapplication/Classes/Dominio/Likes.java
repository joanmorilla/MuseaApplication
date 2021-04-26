package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;

public class Likes implements Serializable {

    private String artworkId;
    private String image;

    public String getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(String artworkId) {
        this.artworkId = artworkId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
