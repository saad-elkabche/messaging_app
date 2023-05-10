package com.example.messagingapp1.databaseStuff;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message")
public class message {
    @PrimaryKey(autoGenerate = true)
    int id;
    String message;
    String senderId;
    String DiscussionRef;
    String time;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public message( String message, String senderId, String discussionRef, String time, String type) {

        this.message = message;
        this.senderId = senderId;
        DiscussionRef = discussionRef;
        this.time = time;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getDiscussionRef() {
        return DiscussionRef;
    }

    public void setDiscussionRef(String discussionRef) {
        DiscussionRef = discussionRef;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public message() {
    }


}
