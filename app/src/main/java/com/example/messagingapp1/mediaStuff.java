package com.example.messagingapp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class mediaStuff {

   // private static final String directory="chat_app";

    @NonNull
    private static File createFile(Context cont, String imageName,String Ref){
        File parentFile=cont.getDir(Ref,Context.MODE_PRIVATE);
        return new File(parentFile,imageName);
    }

    public static Bitmap convertToBitmap(Drawable dra){
        return ((BitmapDrawable)dra).getBitmap();
    }

    public static void saveImageToInternalStorage(Context cont, String ImageName, Bitmap bit,String Ref){

        try {
            FileOutputStream fileOutputStream=new FileOutputStream(createFile(cont,ImageName,Ref));
            bit.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap loadImageFromInterStorage(Context cont, String ImageName,String Ref){
        Bitmap bit=null;
        try {
            FileInputStream fileIn=new FileInputStream(createFile(cont,ImageName,Ref));
             bit=BitmapFactory.decodeStream(fileIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bit;
    }

}
