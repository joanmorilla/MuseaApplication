package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;

public class Restriction implements Serializable {
    private Descriptions text;

    public Descriptions getText() {
        return text;
    }

    public void setText(Descriptions text) {
        this.text = text;
    }
}
