package com.example.museaapplication.Classes;

import androidx.fragment.app.Fragment;

import com.example.museaapplication.Classes.Dominio.Museo;

import java.util.Stack;

public class SingletonDataHolder {
    private static SingletonDataHolder _instance;
    public static Fragment active;


    public static SingletonDataHolder getInstance() {
        if (_instance == null) {
            _instance = new SingletonDataHolder();
            backStack = new Stack<>();
        }
        return _instance;
    }

    private String codedImage;

    private Museo[] museums;

    public int main_initial_frag = 0;
    public static Stack<Integer> backStack;



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
