package com.example.museaapplication.Classes;

import androidx.fragment.app.Fragment;

import com.example.museaapplication.Classes.Json.Museo;
import com.example.museaapplication.ui.PropersFragment;

public class SingletonDataHolder {
    private static SingletonDataHolder _instance;


    public static SingletonDataHolder getInstance() {
        if (_instance == null) {
            _instance = new SingletonDataHolder();
        }
        return _instance;
    }

    private String codedImage;

    private Museo[] museums;

    public int main_initial_frag = 0;



    public String getCodedImage(){
        return codedImage;
    }
    public void setCodedImage(String c){
        codedImage = c;
    }

    public Museo[] getMuseums() {
        return museums;
    }

    public void setMuseums(Museo[] museums) {
        this.museums = museums;
    }
}
