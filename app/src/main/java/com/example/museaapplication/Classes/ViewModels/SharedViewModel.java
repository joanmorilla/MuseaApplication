package com.example.museaapplication.Classes.ViewModels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.ui.ExpositionFragment;
import com.example.museaapplication.ui.MuseoFragment;

public class SharedViewModel extends ViewModel {

    private Museo curMuseum;
    private MutableLiveData<Exhibition> curExposition = new MutableLiveData<>();

    private Fragment mMuseoFragment;
    private Fragment mExpositionFragment;
    private Fragment mCommentsFragment;

    private Fragment active;


    public Museo getCurMuseo() {
        return curMuseum;
    }
    public void setCurMuseum(Museo m){
        curMuseum = m;
    }

    public LiveData<Exhibition> getCurExposition() {
        return curExposition;
    }

    public void setCurExposition(Exhibition curExposition) {
        this.curExposition.postValue(curExposition);
    }

    public Fragment getmMuseoFragment() {
        return mMuseoFragment;
    }

    public void setmMuseoFragment(Fragment mMuseoFragment) {
        if (this.mMuseoFragment == null)
            this.mMuseoFragment = mMuseoFragment;
    }

    public Fragment getmExpositionFragment() {
        return mExpositionFragment;
    }

    public void setmExpositionFragment(Fragment mExpositionFragment) {
        if (this.mExpositionFragment == null)
            this.mExpositionFragment = mExpositionFragment;
    }

    public Fragment getActive() {
        if (active == null) active = mMuseoFragment;
        return active;
    }

    public void setActive(Fragment active) {
        this.active = active;
    }

    public Fragment getmCommentsFragment() {
        return mCommentsFragment;
    }

    public void setmCommentsFragment(Fragment mCommentsFragment) {
        if (this.mCommentsFragment == null)
            this.mCommentsFragment = mCommentsFragment;
    }
}
