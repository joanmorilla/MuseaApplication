package com.example.museaapplication;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.museaapplication.Classes.Adapters.Chats.MessageAdapter;
import com.example.museaapplication.Classes.Adapters.Chats.MessageFormat;
import com.example.museaapplication.Classes.SocketService;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.graphics.Color.argb;

public class ChatActivity extends AppCompatActivity {

    private EditText textField;
    private ImageButton sendButton;

    public static final String TAG  = "ChatActivity";
    public static String uniqueId;

    private String Username;

    private Boolean hasConnection = false;

    private ListView messageListView;
    private MessageAdapter messageAdapter;

    private Thread thread2;
    private boolean startTyping = false;
    private int time = 2;

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
                setTitle("SocketIO");
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
        stopService(new Intent(this, SocketService.class));
        Log.e(TAG, "" + mSocket);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "A channel";
            String description = "Just a channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MyChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationChannel channel2 = new NotificationChannel("BackGround", "Background", importance);
            notificationManager.createNotificationChannel(channel2);
        }
        Username = "User1";

        uniqueId = UUID.randomUUID().toString();
        Log.i(TAG, "onCreate: " + uniqueId);

        if(savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection");
        }

        if(hasConnection){

        }else {
            mSocket.connect();
            mSocket.on("connect user", onNewUser);
            //mSocket.on("chat message", onNewMessage);
            mSocket.on("on typing", onTyping);

            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username + " Connected");
                mSocket.emit("connect user", userId);
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

        onTypeButtonEnable();
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

    private NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: ");
                    Log.i(TAG, "run: " + args.length);
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    String id;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                        id = data.getString("uniqueId");
                        Log.i(TAG, "run: " + username + message + id);

                        MessageFormat format = new MessageFormat(id, username, message);
                        Log.i(TAG, "run:4 ");
                        messageAdapter.add(format);
                        Log.i(TAG, "run:5 ");

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
                    MessageFormat format = new MessageFormat(null, username, null);
                    messageAdapter.add(format);
                    //messageListView.smoothScrollToPosition(0);
                    messageListView.scrollTo(0, messageAdapter.getCount()-1);
                    Log.i(TAG, "run: " + username);
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

                        if(id.equals(uniqueId)){
                            typingOrNot = false;
                        }else {
                            setTitle(userName);
                        }

                        if(typingOrNot){

                            if(!startTyping){
                                startTyping = true;
                                thread2=new Thread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                while(time > 0) {
                                                    synchronized (this){
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
                            }else {
                                time = 2;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "sendMessage: 1"+ mSocket.emit("chat message", jsonObject));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSocket.on("chat message", onNewMessage);
        if (!hasConnection){
            Log.d(TAG, "Resume");
            mSocket.connect();
            mSocket.on("connect user", onNewUser);
            //mSocket.on("chat message", onNewMessage);
            mSocket.on("on typing", onTyping);

            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username + " Connected");
                mSocket.emit("connect user", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
        }
        Intent service = new Intent(this , SocketService.class);
        //stopService(service);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);
        mSocket.off("connect user", onNewUser);
        mSocket.off("on typing", onTyping);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, SocketService.class));
            }else{
                startService(new Intent(this, SocketService.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isFinishing()){
            Log.i(TAG, "onDestroy: ");

            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username + " DisConnected");
                mSocket.emit("connect user", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.disconnect();
            mSocket.off("chat message", onNewMessage);
            mSocket.off("connect user", onNewUser);
            mSocket.off("on typing", onTyping);
            Username = "";
            messageAdapter.clear();

        }else {
            Log.i(TAG, "onDestroy: is rotating.....");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_state) {
            stopService(new Intent(this, SocketService.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
