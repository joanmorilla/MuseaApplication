package com.example.museaapplication.Classes.Json;

import java.io.Serializable;

public class Exhibition implements Serializable {
    private String[] works;
    private String _id;
    private String name;
    private String room;
    private Descriptions descriptions;

    private Work[] workObjects;

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
}
