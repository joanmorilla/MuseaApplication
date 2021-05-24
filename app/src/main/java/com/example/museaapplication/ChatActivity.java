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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.museaapplication.Classes.Adapters.BD.ChatsDBHelper;
import com.example.museaapplication.Classes.Adapters.Chats.MessageAdapter;
import com.example.museaapplication.Classes.Adapters.Chats.MessageFormat;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SocketService;
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

    private Boolean hasConnection = false;

    private ListView messageListView;
    private MessageAdapter messageAdapter;
    ChatsDBHelper dbHelper;

    private Thread thread2;
    private boolean startTyping = false;
    private int time = 2;

    // For deleting
    private boolean isSelected;
    private ArrayList<MessageFormat> selectedMessages;

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
                setTitle(getIntent().getStringExtra("ChatName"));
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
        SocketService.onNewMessageActive = onNewMessage;
        setTitle(getIntent().getStringExtra("ChatName"));
        Log.e(TAG, "" + mSocket);
        dbHelper = ChatsDBHelper.getInstance(ChatActivity.this);
        dbHelper.insertChat("Chat1");

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
            mSocket.on("join room", onJoin);
            mSocket.on("on typing", onTyping);
            mSocket.on("chat message", onNewMessage);
            mSocket.emit("join room", getIntent().getStringExtra("ChatName"));

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
        messageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MessageFormat message = (MessageFormat) parent.getItemAtPosition(position);
                messageAdapter.remove(message);
                messageAdapter.notifyDataSetChanged();
                dbHelper.deleteMessageOfChat(message.getUniqueId());
                return false;
            }
        });

        onTypeButtonEnable();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        SocketService.curRoom = getIntent().getStringExtra("ChatName");
        //stopService(new Intent(this, SocketService.class));

        //startForegroundService(new Intent(this, SocketService.class));

        /*Notification summaryNot = new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setGroupSummary(true)
                .setGroup("Messages")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager notificationManager = null;
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.notify(102, summaryNot);
        }*/
        // notificationId is a unique int for each notification that you must define


        //mSocket.on("chat message", onNewMessage);
        if (!hasConnection){
            Log.d(TAG, "Resume");
            mSocket.connect();
            mSocket.emit("join room", getIntent().getStringExtra("ChatName"));
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
        ArrayList<MessageFormat> messages = dbHelper.getMessagesOfChat(getIntent().getStringExtra("ChatName"));
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
                    onTyping.put("room", getIntent().getStringExtra("ChatName"));
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
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                        id = data.getString("uniqueId");
                        room = data.getString("room");
                        MessageFormat messageFormat = new MessageFormat(UUID.randomUUID().toString(), username, message, room);
                        if (room.equals(getIntent().getStringExtra("ChatName"))) {
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
                    MessageFormat format = new MessageFormat(null, username, null, "");
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
                        if (room.equals(getIntent().getStringExtra("ChatName"))){
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
            jsonObject.put("room", getIntent().getStringExtra("ChatName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObject content = new JsonObject();
        Log.d(TAG, getIntent().getStringExtra("ChatName").replace(" ", ""));
        content.addProperty("to", "/topics/" + getIntent().getStringExtra("ChatName").replace(" ", ""));
        content.addProperty("priority", "high");
        JsonObject data = new JsonObject();
        data.addProperty("room", getIntent().getStringExtra("ChatName"));
        data.addProperty("content", message);
        data.addProperty("username", Username);
        content.add("data", data);

        Call<Void> call = RetrofitClient.getInstance().getMyApi().sendMessage(content);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ChatActivity.this, ""+response.code(), Toast.LENGTH_SHORT).show();
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
        return super.onPrepareOptionsMenu(menu);
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
