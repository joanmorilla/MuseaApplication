package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Comment;

import java.io.Serializable;

public class CommentsValue implements Serializable {
    private Comment[] comments;

    public Comment[] getComments() {
        return comments;
    }
}
