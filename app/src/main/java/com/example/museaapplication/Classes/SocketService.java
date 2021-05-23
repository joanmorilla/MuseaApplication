package com.example.museaapplication.Classes;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.museaapplication.ChatActivity;
import com.example.museaapplication.Classes.Adapters.BD.ChatsDBHelper;
import com.example.museaapplication.Classes.Adapters.Chats.MessageFormat;
import com.example.museaapplication.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;


import static android.graphics.Color.argb;

public class SocketService extends Service {
    ChatsDBHelper dbHelper;
    private NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    public static ArrayList<String> openRooms = new ArrayList<>();
    public static String curRoom = "";

    public static Emitter.Listener onNewMessageActive;
    public static Emitter.Listener onTyping;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://hidden-fortress-23910.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        dbHelper = ChatsDBHelper.getInstance(getApplicationContext());
        openRooms.add("Chat1");
        openRooms.add("Chat2");
        /*JSONObject userId = new JSONObject();
        try {
            userId.put("username", "Username" + " Connected");
            this.mSocket.emit("connect user", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("SocketService", "Destroyed");
        stopForeground(true);
        //mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);
        mSocket.off("chat message", onNewMessageActive);
        super.onDestroy();
    }
    // Se pueden asignar listeners estaticos, por eso voy a hacerlos todos estaticos dentro del servicio
    // de esta forma tod se controlaria desde el servicio mas comodo y asi tmb puedo cancelarlos cuando quiera

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);
        mSocket.off("chat message", onNewMessageActive);
        inboxStyle = new NotificationCompat.InboxStyle();
        mSocket.connect();
        mSocket.on("chat message", onNewMessage);
        mSocket.on("chat message", onNewMessageActive);

        for(String room: openRooms){
            mSocket.emit("join room", room);
        }
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Running")
                .setColor(argb(150, 30, 50, 255))
                .setContentText("Listening in Background")
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setNotificationSilent()
                .setGroup("Messages");

// notificationId is a unique int for each notification that you must define
        startForeground(101, notBuilder.build());
        return Service.START_STICKY;
    }


    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("ServiceSocket", "Call");
            JSONObject data = (JSONObject) args[0];
            String message;
            String username;
            String room;
            try {
                Log.d("SocketService", "FetchData");
                username = data.getString("username");
                message = data.getString("message");
                room = data.getString("room");
                Log.d("SocketService", message);
                if (!room.equals(curRoom)) {
                    final Intent notificationIntent = new Intent(getApplicationContext(), ChatActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    notificationIntent.putExtra("ChatName", room);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(room)
                            .setColor(argb(150, 30, 50, 255))
                            .setContentText(username + ": " + message)
                            .setStyle(inboxStyle.addLine(username + ": " + message))
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setGroup("Messages");
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

// notificationId is a unique int for each notification that you must define
                    notificationManager.notify(openRooms.indexOf(room), notBuilder.build());
                }
                dbHelper.insertMessage(room, new MessageFormat(UUID.randomUUID().toString(), username, message, room));
            } catch (Exception e) {
                return;
            }
        }
    };
}
