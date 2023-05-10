package com.example.messagingapp1.databaseStuff;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {message.class},version = 2,exportSchema = false)
public abstract class myRoom extends RoomDatabase {
    public abstract DAO myDao();
    static myRoom mydb;
    public static myRoom getDatabase(Context context){
        if(mydb==null){
            synchronized(myRoom.class){
                if(mydb==null){
                    mydb= Room.databaseBuilder(context.getApplicationContext(),myRoom.class,"mydb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                }
            }
        }
        return mydb;
    }
}
