package com.example.museaapplication.Classes.Persitencia;

import com.example.museaapplication.Classes.Json.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;

import retrofit2.Call;

public class MuseoPers implements IMuseu{

    @Override
    public Museo get() {
        return null;
    }

    @Override
    public Museo[] getAll() {
        return new Museo[0];
    }
}
