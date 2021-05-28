package com.example.museaapplication.ui.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.IOUtils;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MainActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class edit_user extends AppCompatActivity {

    CircularImageView image_perfil;
    TextView insert_image,textwarn;
    Button save_result;
    EditText name,bio;
    String baseUrl = "https://museaimages1.s3.amazonaws.com/users/";
    private edit_userViewModel euvm;

    Uri selectedImageUri;

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
                //Toast.makeText(getApplicationContext(),"S'han guardat els canvis", Toast.LENGTH_LONG).show();
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
                if (selectedImageUri != null)
                    uploadImage(selectedImageUri, "profile_pic_" + UUID.randomUUID().toString());
            }
        });

        euvm.getFinish().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == "OK") {
                    //Toast.makeText(edit_user.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
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

    private void uploadImage(Uri imageUri, String fileName){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:d0b2be91-2d6c-4a94-8d54-28fea41fc2b6", // ID del grupo de identidades
                Regions.US_EAST_1 // Región
        );

        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();
        //File file1 = new File("" + selectedImageUri);
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, "/" + fileName + "v1");

        try{
            path.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        createFile(getApplicationContext(), imageUri, file);

        TransferObserver uploadObserver = transferUtility.upload("users/" + fileName + "." + getFileExtension(selectedImageUri), file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {

                    Call<Void> call = RetrofitClient.getInstance().getMyApi().updateProfilePic("RaulPes", baseUrl + fileName + "." + getFileExtension(selectedImageUri));
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(getApplicationContext(), "Upload Completed!", Toast.LENGTH_SHORT).show();
                            SingletonDataHolder.userViewModel.loadUsersinfo();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                    file.delete();
                } else if (TransferState.FAILED == state) {
                    file.delete();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
            }

            @Override
            public void onError(int id, Exception ex) {
                ex.printStackTrace();
            }

        });
    }

    private void createFile(Context context, Uri srcUri, File file) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream("./" + file);
            IOUtils.copy(inputStream, outputStream);
            Log.d("TryCreate", "Entro");
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            Log.e("CreateDile", "Exception");
            e.printStackTrace();
        }
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                Log.e("EDITUSER", "Uri: " + selectedImageUri);
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    image_perfil.setImageURI(selectedImageUri);
                }
            }
        }
    }

}