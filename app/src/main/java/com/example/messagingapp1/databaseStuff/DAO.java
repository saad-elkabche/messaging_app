package com.example.messagingapp1.databaseStuff;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    void insertMessage(message m);
    @Query("select * from message where DiscussionRef=:ref" )
    LiveData<List<message>> getMessages(String ref);

    @Query("select * from message where DiscussionRef=:refe")
    List<message> getAllMessages(String refe);
}
