package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Work;

import java.io.Serializable;

public class WorksArray implements Serializable {

    private Work[] works;

    public Work[] getWorks() {
        return works;
    }

    public void setWorks(Work[] works) {
        this.works = works;
    }
}
