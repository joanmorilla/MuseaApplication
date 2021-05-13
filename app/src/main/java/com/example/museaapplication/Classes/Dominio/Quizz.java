package com.example.museaapplication.Classes.Dominio;

import android.util.Pair;

import java.io.Serializable;
import java.util.List;

public class Quizz implements Serializable {
    private Answer[] answers;
    private String _id;
    private Descriptions question;
    private int points;
    private String image;

    public Quizz(String id, Descriptions question, int points ) {
        //this.answers = answers;
        this._id = id;
        this.question = question;
        this.points = points;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }

    public Descriptions getQuestion() {
        return question;
    }

    public void setQuestion(Descriptions question) {
        this.question = question;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
