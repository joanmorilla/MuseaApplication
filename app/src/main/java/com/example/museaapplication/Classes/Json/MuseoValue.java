package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Museo;

public class MuseoValue {
    private Museo[] museums;

    private AuxMuseo museum;

    public Museo getMuseo(int i) {
            return museums[i];
    }

    public Museo[] getMuseums() {
        return museums;
    }

    public AuxMuseo getMuseum() {
        return museum;
    }
}
