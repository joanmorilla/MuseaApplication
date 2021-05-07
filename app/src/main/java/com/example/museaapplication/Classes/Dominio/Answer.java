package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;

public class Answer extends Descriptions implements Serializable {

    private boolean correct;

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean c) {
        correct = c;
    }
}
