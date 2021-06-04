package com.example.museaapplication;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.museaapplication.Classes.Adapters.BD.ChatsDBHelper;
import com.example.museaapplication.Classes.Adapters.Chats.ChatFormat;
import com.example.museaapplication.Classes.Adapters.Chats.MessageAdapter;
import com.example.museaapplication.Classes.Adapters.Chats.MessageFormat;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.Services.MyFirebaseNotifications;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.Classes.SocketService;
import com.example.museaapplication.ui.InitialActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

import static android.graphics.Color.argb;

public class ChatActivity extends AppCompatActivity {

    private EditText textField;
    private ImageButton sendButton;

    public static final String TAG  = "ChatActivity";
    public static String uniqueId;

    private String Username;
    private String roomActivity;

    private Boolean hasConnection = false;

    private ListView messageListView;
    private MessageAdapter messageAdapter;
    ChatsDBHelper dbHelper;

    private Thread thread2;
    private boolean startTyping = false;
    private int time = 2;
    String profilePic;

    // For deleting
    private boolean isSelected = false;
    private ArrayList<MessageFormat> selectedMessages = new ArrayList<>();

    Menu menu;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://hidden-fortress-23910.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

    @SuppressLint("HandlerLeak")
    Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: typing stopped " + startTyping);
            if(time == 0){
                setTitle(roomActivity);
                Log.i(TAG, "handleMessage: typing stopped time is " + time);
                startTyping = false;
                time = 2;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (getIntent() != null && getIntent().hasExtra("ChatName")) roomActivity = getIntent().getStringExtra("ChatName");
        else roomActivity = "AAAAAAA";
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //SocketService.onNewMessageActive = onNewMessage;
        setTitle(roomActivity);
        dbHelper = ChatsDBHelper.getInstance(this);
        if (SingletonDataHolder.getInstance().getLoggedUser() != null) {
            Username = SingletonDataHolder.getInstance().getLoggedUser().getUserId();
            profilePic = SingletonDataHolder.getInstance().getLoggedUser().getProfilePic();
        }
        else {
            Intent i = new Intent(this, InitialActivity.class);
            startActivity(i);
            finish();
            /*SharedPreferences sharedPref = getSharedPreferences("", Context.MODE_PRIVATE);
            String sharedValue = sharedPref.getString(getString(R.string.auto_signin_key), "");
            profilePic = "https://images-na.ssl-images-amazon.com/images/S/sgp-catalog-images/region_US/g9a9m-MHM425BWQ9F-Full-Image_GalleryBackground-en-US-1521579412582._SX1080_.jpg";
            if (!sharedValue.isEmpty()) {
                int index = sharedValue.lastIndexOf('#');
                Username = sharedValue.substring(0, index);
            }else {
                Username = "Anonymous";
            }*/
        }

        int index = dbHelper.getRowIdOfChat(roomActivity)-1;
        NotificationCompat.InboxStyle newInbox = new NotificationCompat.InboxStyle();
        SocketService.inboxes.set(index, newInbox);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.cancel(index);

        uniqueId = UUID.randomUUID().toString();
        Log.i(TAG, "onCreate: " + uniqueId);

        if(savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection");
        }

        if(hasConnection){

        }else {
            mSocket.connect();
            mSocket.on("connect user", onNewUser);
            mSocket.on("join room", onJoin);
            mSocket.on("on typing", onTyping);
            mSocket.on("chat message", onNewMessage);
            mSocket.emit("join room", roomActivity);

            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username + " Connected");
                //mSocket.emit("connect user", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG, "onCreate: " + hasConnection);
        hasConnection = true;


        Log.i(TAG, "onCreate: " + Username + " " + "Connected");

        textField = findViewById(R.id.textField);
        sendButton = findViewById(R.id.sendButton);
        messageListView = findViewById(R.id.messageListView);

        List<MessageFormat> messageFormatList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.item_message, messageFormatList);
        messageListView.setAdapter(messageAdapter);
        messageListView.setLongClickable(true);
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelected) {
                    MessageFormat message = (MessageFormat) parent.getItemAtPosition(position);
                    if (selectedMessages == null) selectedMessages = new ArrayList<>();
                    messageListView.setItemChecked(position, true);
                    int res = messageAdapter.addSelected(position);
                    if (res >= 0) {
                        selectedMessages.remove(res);
                    }else selectedMessages.add(message);
                    if (messageAdapter.noSelecteds()) {
                        isSelected = false;
                        selectedMessages = new ArrayList<>();
                        invalidateOptionsMenu();
                    }
                    messageAdapter.notifyDataSetChanged();
                }
            }
        });
        messageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isSelected) {
                    isSelected = true;
                    invalidateOptionsMenu();
                }
                return false;
            }
        });

        onTypeButtonEnable();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        SocketService.curRoom = roomActivity;
        ChatsDBHelper.getInstance(this).clearNewMessages(roomActivity);

        if (!hasConnection){
            Log.d(TAG, "Resume");
            mSocket.connect();
            mSocket.emit("join room", roomActivity);
            mSocket.on("connect user", onNewUser);
            mSocket.on("chat message", onNewMessage);
            mSocket.on("on typing", onTyping);

            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username + " Connected");
                //mSocket.emit("connect user", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            //notificationManager.cancel(2);
        }
        messageAdapter.clear();
        ArrayList<MessageFormat> messages = dbHelper.getMessagesOfChat(roomActivity);
        for (MessageFormat m: messages){
            messageAdapter.add(m);
        }
        messageListView.smoothScrollToPosition(messages.size()-1);
        Intent service = new Intent(this , SocketService.class);
        //stopService(service);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasConnection", hasConnection);
    }

    public void onTypeButtonEnable(){
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                JSONObject onTyping = new JSONObject();
                try {
                    onTyping.put("typing", true);
                    onTyping.put("username", Username);
                    onTyping.put("uniqueId", uniqueId);
                    onTyping.put("room", roomActivity);
                    mSocket.emit("on typing", onTyping);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: ");
                    Log.i(TAG, "run: " + args.length);
                    JSONObject data = (JSONObject) args[0];
                    Log.d(TAG, "data:" + data.toString());
                    String username;
                    String message;
                    String id;
                    String room;
                    String profPic;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                        id = data.getString("uniqueId");
                        room = data.getString("room");
                        profPic = data.getString("profilePic");
                        MessageFormat messageFormat = new MessageFormat(UUID.randomUUID().toString(), username, message, room, profPic);
                        if (room.equals(roomActivity)) {
                            Log.i(TAG, "run: " + username + message + id);
                            Log.i(TAG, "run:4 ");
                            messageAdapter.add(messageFormat);
                            Log.i(TAG, "run:5 ");
                            dbHelper.insertMessage(room, messageFormat);
                        }
                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

    Emitter.Listener onNewUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int length = args.length;

                    if(length == 0){
                        return;
                    }
                    //Here i'm getting weird error..................///////run :1 and run: 0
                    Log.i(TAG, "run: ");
                    Log.i(TAG, "run: " + args.length);

                    String username =args[0].toString();
                    try {
                        JSONObject object = new JSONObject(username);
                        username = object.getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MessageFormat format = new MessageFormat(null, username, null, "", "");
                    messageAdapter.add(format);
                    //messageListView.smoothScrollToPosition(0);
                    messageListView.scrollTo(0, messageAdapter.getCount()-1);
                    Log.i(TAG, "run: " + username);
                }
            });
        }
    };

    Emitter.Listener onJoin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String room = args[0].toString();
                    Log.d(TAG, "Joined " + room);
                }
            });
        }
    };


    Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.i(TAG, "run: " + args[0]);
                    try {
                        Boolean typingOrNot = data.getBoolean("typing");
                        String userName = data.getString("username") + " is Typing......";
                        String id = data.getString("uniqueId");
                        String room = data.getString("room");
                        if (room.equals(roomActivity)){
                            if(id.equals(uniqueId)){
                                typingOrNot = false;
                            }else {
                                setTitle(userName);
                            }

                            if(typingOrNot) {

                                if (!startTyping) {
                                    startTyping = true;
                                    thread2 = new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    while (time > 0) {
                                                        synchronized (this) {
                                                            try {
                                                                wait(1000);
                                                                Log.i(TAG, "run: typing " + time);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            time--;
                                                        }
                                                        handler2.sendEmptyMessage(0);
                                                    }

                                                }
                                            }
                                    );
                                    thread2.start();
                                } else {
                                    time = 2;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private void addMessage(String username, String message) {

    }

    public void sendMessage(View view){
        Log.i(TAG, "sendMessage: ");
        String message = textField.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            Log.i(TAG, "sendMessage:2 ");
            return;
        }
        textField.setText("");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", message);
            jsonObject.put("username", Username);
            jsonObject.put("uniqueId", uniqueId);
            jsonObject.put("room", roomActivity);
            jsonObject.put("profilePic", profilePic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObject content = new JsonObject();
        Log.d(TAG, roomActivity.replace(" ", ""));
        content.addProperty("to", "/topics/" + roomActivity.replace(" ", ""));
        JsonObject data = new JsonObject();
        data.addProperty("room", roomActivity);
        data.addProperty("content", message);
        data.addProperty("username", Username);
        data.addProperty("profilePic", profilePic);
        content.add("data", data);

        Call<Void> call = RetrofitClient.getInstance().getMyApi().sendMessage(content);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Toast.makeText(ChatActivity.this, ""+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        Log.i(TAG, "sendMessage: 1"+ mSocket.emit("chat message", jsonObject));
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");
        mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);
        mSocket.off("connect user", onNewUser);
        mSocket.off("on typing", onTyping);
        mSocket.off("join room", onJoin);

        SocketService.curRoom = "";
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, SocketService.class));
            }else{
                startService(new Intent(this, SocketService.class));
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isFinishing()){
            Log.i(TAG, "onDestroy: ");

            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username + " DisConnected");
                //mSocket.emit("connect user", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*mSocket.disconnect();
            mSocket.off("chat message", onNewMessage);
            mSocket.off("connect user", onNewUser);
            mSocket.off("on typing", onTyping);*/
            Username = "";
            messageAdapter.clear();

        }else {
            Log.i(TAG, "onDestroy: is rotating.....");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.delete_message).setVisible(isSelected);
        /*if (isSelected)
            menu.findItem(android.R.id.home).setIcon(R.drawable.ic_outline_cancel_24);
        else
            menu.findItem(android.R.id.home).setIcon()*/
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_activity_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        else if (item.getItemId() == R.id.leave_room){
            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(R.string.leave_room);
            builder.setMessage(R.string.leave_room_confirmation);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbHelper.deleteChat(roomActivity);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(roomActivity.replace(" ", ""));
                    finish();
                }
            });
            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (item.getItemId() == R.id.delete_message){
            for (MessageFormat message : selectedMessages) {
                messageAdapter.remove(message);
                messageAdapter.clear();
                messageAdapter.notifyDataSetChanged();
                dbHelper.deleteMessageOfChat(message.getUniqueId());
            }
            isSelected = false;
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isSelected){
            selectedMessages = new ArrayList<>();
            messageAdapter.clear();
            messageAdapter.notifyDataSetChanged();
            isSelected = false;
            invalidateOptionsMenu();
        } else super.onBackPressed();
    }
}
