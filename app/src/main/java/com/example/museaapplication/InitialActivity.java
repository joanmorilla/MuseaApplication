package com.example.museaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.museaapplication.ui.login.LoginFragment;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        getSupportFragmentManager().beginTransaction().add(R.id.InitialActivity,new LoginFragment()).show(new LoginFragment()).commit();

    }
}