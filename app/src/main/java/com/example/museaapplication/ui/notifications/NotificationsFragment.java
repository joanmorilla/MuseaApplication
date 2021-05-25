package com.example.museaapplication.ui.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.ChatActivity;
import com.example.museaapplication.Classes.Adapters.BD.ChatsDBHelper;
import com.example.museaapplication.Classes.Adapters.Chats.ChatFormat;
import com.example.museaapplication.Classes.Adapters.Chats.MessageFormat;
import com.example.museaapplication.Classes.Services.MyFirebaseNotifications;
import com.example.museaapplication.Classes.SocketService;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private LinearLayout ll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatsDBHelper dbHelper = ChatsDBHelper.getInstance(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "A channel";
            String description = "Just a channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MyChannel", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.enableLights(true);
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            NotificationChannel channel2 = new NotificationChannel("BackGround", "Background", importance);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel2);
        }
        dbHelper.insertChat("Chat1", "https://museaimages1.s3.amazonaws.com/museums%20/Interior_Museo_Egipcio_de_Barcelona_colecci%C3%B3n_permanente.jpg");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ll = root.findViewById(R.id.llayout_chats_view);
        ChatsDBHelper dbHelper = ChatsDBHelper.getInstance(getContext());
        ArrayList<ChatFormat> chats = dbHelper.getChats();

        FirebaseMessaging.getInstance().subscribeToTopic("Messages");

        for (int i = 0; i < chats.size(); i++){
            ChatFormat chat = chats.get(i);
            SocketService.openRooms.add(chat.getChatName());
            SocketService.inboxes.add(new NotificationCompat.InboxStyle());
            FirebaseMessaging.getInstance().subscribeToTopic(chat.getChatName().replace(" ", ""));
            RelativeLayout holder = new RelativeLayout(getContext());
            View v = inflater.inflate(R.layout.custom_chat_button, holder);
            MaterialButton content = v.findViewById(R.id.chat_name);
            TextView chatName = v.findViewById(R.id.chatName);
            TextView usersCount = v.findViewById(R.id.users_count);
            ImageView image = v.findViewById(R.id.image_chat);
            //Picasso.get().load(validateUrl(chat.getImage())).into(image);
            chatName.setText(chats.get(i).getChatName());
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("ChatName", chat.getChatName());
                    startActivity(intent);
                }
            });
            usersCount.setText("" + chats.get(i).getConnectedUsers());
            ll.addView(holder);
        }
        return root;
    }
    private String validateUrl(String url) {
        if (!url.contains("https")) url = url.replace("http", "https");
        return url;
    }
}