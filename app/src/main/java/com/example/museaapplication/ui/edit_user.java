package com.example.museaapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museaapplication.R;

public class edit_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Button button = (Button) findViewById(R.id.btn_up);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent main = new Intent(v.getContext(), MainActivity.class);
                EditText name_up_et = (EditText) findViewById(R.id.et_name_up);

            }
        });



    }
}