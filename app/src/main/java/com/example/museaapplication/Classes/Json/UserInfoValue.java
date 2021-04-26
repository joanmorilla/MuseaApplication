package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserInfoValue implements Serializable {
    @SerializedName("user")
    private UserInfo userinfo;

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }
}
