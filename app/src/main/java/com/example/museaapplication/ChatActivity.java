package com.example.museaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ChatActivity extends AppCompatActivity {

    boolean isEditing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        else if(item.getItemId() == R.id.change_state) {
            invalidateOptionsMenu();
            isEditing = !isEditing;
        }
        else return super.onOptionsItemSelected(item);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        //super.onPrepareOptionsMenu(menu);
        Log.e("ASDASD", "ASDASd");
        MenuItem item = menu.findItem(R.id.delete_message);
        item.setVisible(isEditing);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_activity_menu, menu);
        return true;
    }
}