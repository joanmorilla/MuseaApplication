package com.example.museaapplication.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<String> uText;
    public UserViewModel() {
        uText = new MutableLiveData<>();
        uText.setValue("This is user fragment");
    }
    public LiveData<String> getText() {
        return uText;
    }
}
