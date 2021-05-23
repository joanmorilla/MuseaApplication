package com.example.museaapplication.Classes.Adapters.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.museaapplication.Classes.Adapters.Chats.ChatFormat;
import com.example.museaapplication.Classes.Adapters.Chats.MessageFormat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChatsDBHelper extends SQLiteOpenHelper {
    public ChatsDBHelper(@Nullable Context context) {
        super(context, "ChatsDB", null, 11);
    }

    private static ChatsDBHelper _instance;
    public static synchronized ChatsDBHelper getInstance(Context context){
        if (_instance == null) _instance = new ChatsDBHelper(context);
        return _instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHATS_TABLE = "CREATE TABLE " + "CHATS" +
                "(" +
                "Chat" + " TEXT PRIMARY KEY" +
                ")";
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + "MESSAGES" +
                "(" +
                "UniqueId" + " TEXT PRIMARY KEY," + // Define a primary key
                "ChatName" + " TEXT REFERENCES " + "CHATS" + "," + // Define a foreign key
                "Username" + " TEXT," +
                "Message" + " TEXT" +
                ")";
        db.execSQL(CREATE_CHATS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + "CHATS");
            db.execSQL("DROP TABLE IF EXISTS " + "MESSAGES");
            onCreate(db);
        }
    }

    public void insertMessage(String chatId, MessageFormat message){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("UniqueId", message.getUniqueId()); // 0
        value.put("ChatName", chatId);  // 1
        value.put("Username", message.getUsername());   // 2
        value.put("Message", message.getMessage());  // 3
        db.insert("MESSAGES", null, value);
    }
    public ArrayList<MessageFormat> getMessagesOfChat(String chatName){
        ArrayList<MessageFormat> messages = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("MESSAGES", null,"ChatName=?", new String[]{chatName}, null, null, null);
        while (c.moveToNext()){
            MessageFormat m = new MessageFormat(c.getString(0), c.getString(2), c.getString(3), chatName);
            messages.add(m);
        }
        c.close();
        return messages;
    }
    public void deleteMessageOfChat(String messageId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("MESSAGES", "UniqueId=?", new String[]{messageId});
    }
    public void insertChat(String chatName){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues value = new ContentValues();
            value.put("Chat", chatName);
            db.insertOrThrow("CHATS", null, value);
        }catch (Exception e){
            return;
        }finally {
            db.endTransaction();
        }
    }
    public ArrayList<ChatFormat> getChats(){
        ArrayList<ChatFormat> chats = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("CHATS", null,null, null, null, null, null);
        while (c.moveToNext()){
            ChatFormat chat = new ChatFormat(c.getString(0), 5);
            chats.add(chat);
        }
        c.close();
        return chats;
    }
}
