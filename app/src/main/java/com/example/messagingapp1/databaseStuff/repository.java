package com.example.messagingapp1.databaseStuff;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class repository {
    DAO myDao;

    public repository(Application app) {
        myRoom db=myRoom.getDatabase(app.getApplicationContext());
        this.myDao=db.myDao();
    }

    public void insertMessage(message m){
        myDao.insertMessage(m);
    }

    public LiveData<List<message>> getMessages(String reference){
        return myDao.getMessages(reference);
    }

    public List<message> getAllMessages(String Ref){
       return myDao.getAllMessages(Ref);
    }
}
