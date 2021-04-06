package com.example.museaapplication.ui.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    private MutableLiveData<String> name;
    private MutableLiveData<String> bio;
    private MutableLiveData<String> pic;

    public void insertname(String name_1){
        name.setValue("Raul");
    }

    public MutableLiveData<String> getName_User() {
        return name;
    }
    public void insertbio(String bio_1){
        name.setValue(bio_1);
    }
    public void insertpic(String pic_1){
        name.setValue(pic_1);
    }




}
