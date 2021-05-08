package com.example.museaapplication.Classes.Dominio;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Exhibition implements Serializable {
    private String[] works;
    private String _id;
    private String name;
    private String room;
    private Descriptions descriptions;
    private String image;

    private ArrayList<Work> workObjects;

    //private List<Work> workObjects = new ArrayList<>();


    /*public void addWork(Work w) {
        workObjects.add(w);
    }*/

    public String[] getWorks() {
        return works;
    }

    public void setWorks(String[] works) {
        this.works = works;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Descriptions getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Descriptions descriptions) {
        this.descriptions = descriptions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Work> getWorkObjects() {
        return workObjects;
    }

    public void setWorkObjects(ArrayList<Work> workObjects) {
        this.workObjects = workObjects;
    }
    public void addWork(Work w){
        if (workObjects == null) workObjects = new ArrayList<>();
        workObjects.add(w);
    }
   /* public void setWorkObjects(List<Work> workObjects) {
        this.workObjects = workObjects;
    }
    public List<Work> getWorkObjects() {
        return workObjects;
    }*/
}
