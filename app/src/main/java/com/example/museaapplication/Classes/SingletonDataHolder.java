package com.example.museaapplication.Classes;

public class SingletonDataHolder {
    private static SingletonDataHolder _instance;

    public static SingletonDataHolder getInstance() {
        if (_instance == null) _instance = new SingletonDataHolder();
        return _instance;
    }

    private String codedImage;

    public String getCodedImage(){
        return codedImage;
    }
    public void setCodedImage(String c){
        codedImage = c;
    }
}
