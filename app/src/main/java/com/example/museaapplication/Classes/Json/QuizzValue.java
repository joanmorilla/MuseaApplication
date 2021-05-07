package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Quizz;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class QuizzValue implements Serializable {
    private Quizz[] quizzes;

    public Quizz[] getQuizzes() { return quizzes; }
}
