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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.ChatActivity;
import com.example.museaapplication.R;
import com.google.android.material.button.MaterialButton;
import com.rey.material.widget.Button;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private LinearLayout ll;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ll = root.findViewById(R.id.llayout_chats_view);
        for (int i = 0; i < 30; i++){
            RelativeLayout holder = new RelativeLayout(getContext());
            View v = inflater.inflate(R.layout.custom_chat_button, holder);
            MaterialButton content = v.findViewById(R.id.chat_name);
            content.setMinHeight(0);
            content.setMinWidth(0);
            content.setText("Chat " + i);
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ChatActivity.class);
                    startActivity(i);
                }
            });
            ll.addView(holder);
        }
        return root;
    }
}