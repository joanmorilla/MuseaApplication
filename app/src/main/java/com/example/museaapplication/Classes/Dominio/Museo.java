package com.example.museaapplication.Classes.Dominio;

import com.example.museaapplication.Classes.Json.Descriptions;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Museo implements Serializable {
    private Location[] location;
    @SerializedName("expositions")
    private String[] exhibitions;
    private String _id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Descriptions descriptions;

    private List<Exhibition> exhibitionObjects = new ArrayList<>();

    public Museo(String n, String address, String c, String count) {
        /*location = new Location[2];
        exhibitions = new String[1];*/
        city = c;
        country = count;
        name = n;
        this.address = address;
        _id ="";
        exhibitionObjects = new ArrayList<>();
    }

    public void addExhibition(Exhibition e){
        exhibitionObjects.add(e);
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

    public Location[] getLocation() {
        return location;
    }

    public void setLocation(Location[] location) {
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

    public Descriptions getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Descriptions descriptions) {
        this.descriptions = descriptions;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setExhibitionObjects(List<Exhibition> exhibitionObjects) {
        this.exhibitionObjects = exhibitionObjects;
    }
    public List<Exhibition> getExhibitionObjects() {
        return exhibitionObjects;
    }
}
