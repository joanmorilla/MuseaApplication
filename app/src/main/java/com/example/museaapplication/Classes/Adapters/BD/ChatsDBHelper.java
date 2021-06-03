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
        super(context, "ChatsDB", null, 16);
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
                "Chat" + " TEXT PRIMARY KEY," +
                "Image" + " TEXT" +
                ")";
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + "MESSAGES" +
                "(" +
                "UniqueId" + " TEXT PRIMARY KEY," + // Define a primary key
                "ChatName" + " TEXT REFERENCES " + "CHATS" + "," + // Define a foreign key
                "Username" + " TEXT," +
                "Message" + " TEXT," +
                "ProfilePic" + " TEXT" +
                ")";
        String CREATE_NEW_MESSAGES_TABLE = "CREATE TABLE " + "NEW_MESSAGES" +
                "(" +
                "UniqueId" + " TEXT PRIMARY KEY," + // Define a primary key
                "ChatName" + " TEXT REFERENCES " + "CHATS" + "," + // Define a foreign key
                "Username" + " TEXT," +
                "Message" + " TEXT" +
                ")";
        db.execSQL(CREATE_CHATS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
        db.execSQL(CREATE_NEW_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + "CHATS");
            db.execSQL("DROP TABLE IF EXISTS " + "MESSAGES");
            db.execSQL("DROP TABLE IF EXISTS " + "NEW_MESSAGES");
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
        value.put("ProfilePic", message.getProfilePic()); // 4
        db.insert("MESSAGES", null, value);
    }
    public ArrayList<MessageFormat> getMessagesOfChat(String chatName){
        ArrayList<MessageFormat> messages = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("MESSAGES", null,"ChatName=?", new String[]{chatName}, null, null, null);
        while (c.moveToNext()){
            // TODO profile pic in database
            MessageFormat m = new MessageFormat(c.getString(0), c.getString(2), c.getString(3), chatName, c.getString(4));
            messages.add(m);
        }
        c.close();
        return messages;
    }
    public void deleteMessageOfChat(String messageId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("MESSAGES", "UniqueId=?", new String[]{messageId});
    }
    public boolean insertChat(String chatName, String chatImage){
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues value = new ContentValues();
            value.put("Chat", chatName);
            value.put("Image", chatImage);
            if (db.insert("CHATS", null, value) > 0) return true;
        }catch (Exception e){
            Log.e("DB", "Something went wrong");
            return false;
        }
        return false;
    }
    public void deleteChat(String chatName){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("MESSAGES", "ChatName=?", new String[]{chatName});
        db.delete("CHATS", "Chat=?", new String[]{chatName});
    }
    public ArrayList<ChatFormat> getChats(){
        ArrayList<ChatFormat> chats = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("CHATS", null,null, null, null, null, null);
        while (c.moveToNext()){
            ChatFormat chat = new ChatFormat(c.getString(0), 5);
            chat.setImage(c.getString(1));
            chats.add(chat);
        }
        c.close();
        return chats;
    }
    public int getRowIdOfChat(String ChatName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("CHATS", new String[]{"rowid", "Chat"}, "Chat=?", new String[]{ChatName}, null,null, null);
        c.moveToFirst();
        int res = c.getInt(c.getColumnIndex("rowid"));
        c.close();
        return res;
    }

    public void clearNewMessages(String room) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("NEW_MESSAGES", "ChatName=?", new String[]{room});
    }
    public void insertNewMessage(MessageFormat message){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("UniqueId", message.getUniqueId()); // 0
        value.put("ChatName", message.getRoom());  // 1
        value.put("Username", message.getUsername());   // 2
        value.put("Message", message.getMessage());  // 3
        db.insert("NEW_MESSAGES", null, value);
    }
    public ArrayList<MessageFormat> getNewMessagesOfChat(String room){
        ArrayList<MessageFormat> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("NEW_MESSAGES", null, "ChatName=?", new String[]{room}, null, null, null);
        while (c.moveToNext()){
            // TODO profile pic in database as well
            MessageFormat m = new MessageFormat(c.getString(0), c.getString(2), c.getString(3), c.getString(1), "");
            res.add(m);
        }
        return res;
    }
}
