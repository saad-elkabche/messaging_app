package com.example.messagingapp1;

public class profilemodel {
    String Status;
    String Urimg;
    String id;
    String name;

    public profilemodel() {
    }

    public profilemodel(String status, String uimg, String id, String name) {
        this.Status = status;
        this.Urimg = uimg;
        this.id = id;
        this.name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUrimg() {
        return Urimg;
    }

    public void setUrimg(String urimg) {
        Urimg = urimg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
