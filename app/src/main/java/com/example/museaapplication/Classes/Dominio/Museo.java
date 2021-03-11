package com.example.museaapplication.Classes.Dominio;

import java.util.ArrayList;

public class Museo {
    private float[] location;
    private String[] exhibitions;
    private String _id;
    private String name;
    private String address;
    private String city;
    private String country;


    public Museo(String n, String address, String c, String count) {
        location = new float[2];
        exhibitions = new String[1];
        city = c;
        country = count;
        name = n;
        this.address = address;
        _id ="";
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[] getLocation() {
        return location;
    }

    public void setLocation(float[] location) {
        this.location = location;
    }

    public String[] getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(String[] exhibitions) {
        this.exhibitions = exhibitions;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
