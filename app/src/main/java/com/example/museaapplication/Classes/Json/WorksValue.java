package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Exhibition;

import java.io.Serializable;

public class WorksValue implements Serializable {
    private WorksArray exposition;


    public WorksArray getExposition() {
        return exposition;
    }

    public void setExposition(WorksArray exposition) {
        this.exposition = exposition;
    }
}
