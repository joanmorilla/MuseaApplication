package com.example.museaapplication.Classes;

import android.app.IntentService;
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


import static android.graphics.Color.argb;

public class SocketService extends Service {

    private Thread mThread;
    private boolean mRunning = false;
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

        Log.e("SocketService", "" + mSocket);
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
        //mSocket.off("chat message", onNewMessage);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);

        mSocket.connect();
        mSocket.on("chat message", onNewMessage);
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(), "BackGround")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Running")
                .setColor(argb(150, 30, 50, 255))
                .setContentText("Listening in Background")
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

// notificationId is a unique int for each notification that you must define
        startForeground(1, notBuilder.build());
        return Service.START_STICKY;
    }

    private NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("ServiceSocket", "Call");
            JSONObject data = (JSONObject) args[0];
            String message;
            String username;
            try {
                Log.d("SocketService", "FetchData");
                username = data.getString("username");
                message = data.getString("message");
                Log.d("SocketService", message);

                final Intent notificationIntent = getPackageManager()
                        .getLaunchIntentForPackage(getPackageName())
                        .setPackage(null)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
                NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Chat1")
                        .setColor(argb(150, 30, 50, 255))
                        .setContentText(username + ": " + message)
                        .setStyle(inboxStyle.addLine(username + ": " + message))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

// notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, notBuilder.build());
            } catch (Exception e) {
                return;
            }
        }
    };
}
