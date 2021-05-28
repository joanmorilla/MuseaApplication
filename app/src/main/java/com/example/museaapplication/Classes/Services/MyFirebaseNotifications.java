package com.example.museaapplication.Classes.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.museaapplication.ChatActivity;
import com.example.museaapplication.Classes.Adapters.BD.ChatsDBHelper;
import com.example.museaapplication.Classes.Adapters.Chats.MessageFormat;
import com.example.museaapplication.Classes.SocketService;
import com.example.museaapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.UUID;

import static android.graphics.Color.argb;

public class MyFirebaseNotifications extends FirebaseMessagingService {
    private NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    public static ArrayList<NotificationCompat.InboxStyle> inboxes = new ArrayList<>();

    private ArrayList<MessageFormat> messages = new ArrayList<>();

    // Mapear nuevos mensajes en la bd

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String room = remoteMessage.getData().get("room");
        String message = remoteMessage.getData().get("content");
        String username = remoteMessage.getData().get("username");
        ChatsDBHelper dbHelper = ChatsDBHelper.getInstance(getApplicationContext());
        dbHelper.insertNewMessage(new MessageFormat(UUID.randomUUID().toString(), username, message, room));
        inboxStyle = new NotificationCompat.InboxStyle();
        messages = dbHelper.getNewMessagesOfChat(room);
        int start = 0;
        if (messages.size() > 7) start = messages.size()-7;
        for( int i = start; i < messages.size(); i++){
            inboxStyle.addLine(messages.get(i).getUsername() + ": " + messages.get(i).getMessage());
        }
        Notification summaryNot = new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setGroupSummary(true)
                .setGroup("Messages")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .build();

        MessageFormat mFormat = new MessageFormat(UUID.randomUUID().toString(), username, message, room);
        //int index = dbHelper.getRowIdOfChat(room) - 1;
        Intent openChatIntent = new Intent(this, ChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openChatIntent.putExtra("ChatName", room);
        PendingIntent openChatPendingIntent = PendingIntent.getActivity(this, 0, openChatIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        int index = dbHelper.getRowIdOfChat(room);
        if (!SocketService.curRoom.equals(room)) {
            NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(room)
                    .setColor(argb(150, 30, 50, 255))
                    .setContentText(username + ": " + message)
                    .setStyle(inboxStyle)
                    .setContentIntent(openChatPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
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
