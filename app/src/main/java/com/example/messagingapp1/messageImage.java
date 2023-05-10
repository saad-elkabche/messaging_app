package com.example.messagingapp1;

public class messageImage {
    String url,time,ImageRef,senderId;

    public messageImage(String url, String time, String imageRef, String senderId) {
        this.url = url;
        this.time = time;
        ImageRef = imageRef;
        this.senderId = senderId;
    }

    public messageImage() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageRef() {
        return ImageRef;
    }

    public void setImageRef(String imageRef) {
        ImageRef = imageRef;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
