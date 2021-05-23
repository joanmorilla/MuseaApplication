package com.example.museaapplication.Classes.Adapters.Chats;

public class ChatFormat {
    private String chatName;
    private int connectedUsers;

    public ChatFormat(String cn, int cu){
        chatName = cn;
        connectedUsers = cu;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public int getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(int connectedUsers) {
        this.connectedUsers = connectedUsers;
    }
}
