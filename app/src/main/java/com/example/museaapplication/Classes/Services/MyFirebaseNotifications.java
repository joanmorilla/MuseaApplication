package com.example.museaapplication.Classes.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.museaapplication.Classes.Adapters.BD.ChatsDBHelper;
import com.example.museaapplication.Classes.Adapters.Chats.MessageFormat;
import com.example.museaapplication.Classes.SocketService;
import com.example.museaapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

import static android.graphics.Color.argb;

public class MyFirebaseNotifications extends FirebaseMessagingService {
    private final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Notification summaryNot = new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setGroupSummary(true)
                .setGroup("Messages")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .build();
        String room = remoteMessage.getData().get("room");
        String message = remoteMessage.getData().get("content");
        String username = remoteMessage.getData().get("username");
        ChatsDBHelper dbHelper = ChatsDBHelper.getInstance(getApplicationContext());
        MessageFormat mFormat = new MessageFormat(UUID.randomUUID().toString(), username, message, room);
        if (!SocketService.curRoom.equals(room)) {
            int index = SocketService.openRooms.indexOf(room);
            NotificationCompat.InboxStyle style;
            // For alert messaging
            if (index < 0) {
                index = 103;
                style = new NotificationCompat.InboxStyle();
            }else{
                style = SocketService.inboxes.get(index);
            }
            NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(room)
                    .setColor(argb(150, 30, 50, 255))
                    .setContentText(username + ": " + message)
                    .setStyle(style.addLine(username + ": " + message))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setGroup("Messages");
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

            notificationManager.notify(102, summaryNot);
            notificationManager.notify(index, notBuilder.build());
            // Adding it to the database
            dbHelper.insertMessage(room, mFormat);
        }
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("FIREBASE", "TOKEN");
    }

}
