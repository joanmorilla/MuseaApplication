package com.example.museaapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.museaapplication.R;
import com.google.android.material.button.MaterialButton;
import com.rey.material.widget.Button;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private LinearLayout ll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatsDBHelper dbHelper = ChatsDBHelper.getInstance(getContext());
        dbHelper.insertChat("Chat1");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ll = root.findViewById(R.id.llayout_chats_view);
        ChatsDBHelper dbHelper = ChatsDBHelper.getInstance(getContext());
        ArrayList<ChatFormat> chats = dbHelper.getChats();
        for (int i = 0; i < chats.size(); i++){
            ChatFormat chat = chats.get(i);
            RelativeLayout holder = new RelativeLayout(getContext());
            View v = inflater.inflate(R.layout.custom_chat_button, holder);
            MaterialButton content = v.findViewById(R.id.chat_name);
            TextView usersCount = v.findViewById(R.id.users_count);
            content.setMinHeight(0);
            content.setMinWidth(0);
            content.setText(chats.get(i).getChatName());
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
}