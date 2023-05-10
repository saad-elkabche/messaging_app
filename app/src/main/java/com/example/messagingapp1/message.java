package com.example.messagingapp1;

public class message {
    String message;
    String time;
    String senderid;

    public message(String message, String time, String senderid) {
        this.message = message;
        this.time = time;
        this.senderid = senderid;
    }

    public message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }
}
