package com.example.museaapplication.Classes.Dominio;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.museaapplication.Classes.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Work implements Serializable {
    private String _id;
    private String title;
    private String author;
    private float score;
    private String type;
    private String image;
    private Descriptions descriptions;

    private boolean loved = false;

    private ArrayList<Comment> commentsObjects;


    public boolean likeWork(){
        loved = !loved;


        return loved;
    }

    public void setLoved(boolean b) {
        loved = b;
    }
    public boolean isLoved() {
        return loved;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void addComment(Comment c){
        if (commentsObjects == null) commentsObjects = new ArrayList<>();
        commentsObjects.add(c);
    }
    public void removeComment(Comment c){
        commentsObjects.remove(c);
    }
    public void setCommentsObjects(ArrayList<Comment> cs){
        commentsObjects = cs;
    }
    public ArrayList<Comment> getCommentsObjects() {
        if (commentsObjects == null) commentsObjects = new ArrayList<>();
        return commentsObjects;
    }
}
