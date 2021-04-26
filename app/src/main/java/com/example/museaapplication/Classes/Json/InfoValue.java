package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Info;

import java.io.Serializable;

public class InfoValue implements Serializable {
    private Info info;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
