package com.example.museaapplication.Classes.Adapters.Chats;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.museaapplication.ChatActivity;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageFormat> {
    private ArrayList<Integer> ids = new ArrayList<>();
    public MessageAdapter(Context context, int resource, List<MessageFormat> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageFormat message = getItem(position);

        if(TextUtils.isEmpty(message.getMessage())){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_connected, parent, false);
            convertView.setClickable(true);

            TextView messageText = convertView.findViewById(R.id.message_body);

            String userConnected = message.getUsername();
            messageText.setText(userConnected);

        }else if(message.getUsername().equals(SingletonDataHolder.getInstance().getLoggedUser().getUserId())){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_message, parent, false);
            TextView messageText = convertView.findViewById(R.id.message_body);
            messageText.setText(message.getMessage());

        }else {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.their_message, parent, false);

            TextView messageText = convertView.findViewById(R.id.message_body);
            TextView usernameText = (TextView) convertView.findViewById(R.id.name);

            messageText.setVisibility(View.VISIBLE);
            usernameText.setVisibility(View.VISIBLE);

            messageText.setText(message.getMessage());
            usernameText.setText(message.getUsername());
        }
        if (ids.contains(position)) {
            convertView.setBackgroundColor(Color.argb(120,88, 169, 245));
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

    public int addSelected(int id){
        int res = ids.indexOf(id);
        Log.e("ADAPter", "" + res);
        if (res >= 0) {
            ids.remove(res);
        }
        else {
            ids.add(id);
        }
        return res;
    }
    public boolean noSelecteds(){
        return ids.size() == 0;
    }
    public void clear(){
        ids = new ArrayList<>();
    }
}
