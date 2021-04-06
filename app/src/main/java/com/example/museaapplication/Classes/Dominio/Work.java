package com.example.museaapplication.Classes.Dominio;

import com.example.museaapplication.Classes.Json.Descriptions;

import java.io.Serializable;

public class Work implements Serializable {
    private String _id;
    private String title;
    private String author;
    private float score;
    private String type;
    private Descriptions descriptions;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Descriptions getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Descriptions descriptions) {
        this.descriptions = descriptions;
    }
}
