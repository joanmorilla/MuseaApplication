package com.example.museaapplication.Classes.Adapters.Chats;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.museaapplication.ChatActivity;
import com.example.museaapplication.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageFormat> {
    public MessageAdapter(Context context, int resource, List<MessageFormat> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(ChatActivity.TAG, "getView:");

        MessageFormat message = getItem(position);

        if(TextUtils.isEmpty(message.getMessage())){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_connected, parent, false);
            convertView.setClickable(true);

            TextView messageText = convertView.findViewById(R.id.message_body);

            Log.i(ChatActivity.TAG, "getView: is empty ");
            String userConnected = message.getUsername();
            messageText.setText(userConnected);

        }else if(message.getUsername().equals("User1")){
            Log.i(ChatActivity.TAG, "getView: " + message.getUniqueId() + " " + ChatActivity.uniqueId);


            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_message, parent, false);
            TextView messageText = convertView.findViewById(R.id.message_body);
            messageText.setText(message.getMessage());

        }else {
            Log.i(ChatActivity.TAG, "getView: is not empty");

            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.their_message, parent, false);

            TextView messageText = convertView.findViewById(R.id.message_body);
            TextView usernameText = (TextView) convertView.findViewById(R.id.name);

            messageText.setVisibility(View.VISIBLE);
            usernameText.setVisibility(View.VISIBLE);

            messageText.setText(message.getMessage());
            usernameText.setText(message.getUsername());
        }

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }
}
