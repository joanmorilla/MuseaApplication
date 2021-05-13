package com.example.museaapplication.ui.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MainActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class edit_user extends AppCompatActivity {

    CircularImageView image_perfil;
    TextView insert_image,textwarn;
    Button save_result;
    EditText name,bio;
    private edit_userViewModel euvm;


    int SELECT_PICTURE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        euvm = new ViewModelProvider(this).get(edit_userViewModel.class);
        image_perfil = findViewById(R.id.image_perf);
        String url = "https://museaimages.s3.eu-west-3.amazonaws.com/logo.png";
        Picasso.get().load(url).fit().into(image_perfil);

        insert_image = findViewById(R.id.select_image);
        name = findViewById(R.id.editTextTextPersonName);
        bio = findViewById(R.id.editTextTextPersonBio);
        save_result = findViewById(R.id.save_res);
        textwarn = findViewById(R.id.text_warnigs_edit);

        String name_aux = getIntent().getStringExtra("username");
        String bio_aux = getIntent().getStringExtra("bio");

        name.setText(name_aux);
        bio.setText(bio_aux);

        save_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"S'han guardat els canvis", Toast.LENGTH_LONG).show();
                String warningMessage = new String();
                if(name.getText().toString().length() < 6){
                   warningMessage = getApplicationContext().getResources().getString(R.string.warning_short_username_edit);
                }
                if(bio.getText().toString().length() < 10){
                    if(!warningMessage.isEmpty()) warningMessage += "\n";
                    warningMessage += getApplicationContext().getResources().getString(R.string.warning_short_bio);
                }
                if(warningMessage.isEmpty()) {
                    textwarn.setText("");
                    euvm.edit_user_info(name.getText().toString(), bio.getText().toString());
                    SingletonDataHolder.userViewModel.UpdateUserInfo();
                }
                else{
                    textwarn.setText(warningMessage);
                }

            }
        });

        euvm.getFinish().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == "OK") {
                    Toast.makeText(edit_user.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if (s == "ERROR"){
                    Toast.makeText(edit_user.this, "No va", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    image_perfil.setImageURI(selectedImageUri);
                }
            }
        }
    }

}