package com.example.museaapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.museaapplication.R;
import com.example.museaapplication.ui.quizz.QuizzMenuFragment;

public class QuizzActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.QuizzActivity, new QuizzMenuFragment()).show(new QuizzMenuFragment())
                    .commitNow();
        }
    }
}