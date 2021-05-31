package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {
    private String userId;
    private String name;
    private String email;
    private String bio;
    private String[] favourites;
    private int points;
    private String profilePic;
    private boolean premium;
    private String[] visited;
    private String[] likes;
    private Date premiumDate;
    private String email;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String[] getFavourites() {
        return favourites;
    }

    public void setFavourites(String[] favourites) {
        this.favourites = favourites;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String[] getVisited() {
        return visited;
    }

    public void setVisited(String[] visited) {
        this.visited = visited;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
  
    public Date getPremiumDate() { return premiumDate; }

    public void setPremiumDate(Date d) { premiumDate = d; }

}
