package com.example.messagingapp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class sendPicturesToFireBase {

     StorageReference ref;
    DatabaseReference dataRef;
    String refDialo;
    Context cont;

    private static Bitmap ConvertImage(Drawable d){
        return ((BitmapDrawable)d).getBitmap();
    }


    public sendPicturesToFireBase(String refDialo,Context cont){
        this.refDialo=refDialo;
        this.cont=cont;
        
        ref=FirebaseStorage.getInstance().getReference().child("chat_Images").child(this.refDialo);

        dataRef= FirebaseDatabase.getInstance().getReference().child("chat_Images").child(this.refDialo);
    }





    public  void SendPictureToFireBase(Drawable d,String time){
        Bitmap bit=ConvertImage(d);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] bytes= byteArrayOutputStream.toByteArray();
        StorageReference new_Ref=ref.child(time).child(time+".png");

        UploadTask task=new_Ref.putBytes(bytes);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                new_Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        sendMessageToRealTimeDataBase(uri,time);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(cont, "not good", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void sendMessageToRealTimeDataBase(Uri result,String timeRef) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm");
        String time=simpleDateFormat.format(Calendar.getInstance().getTime());
      messageImage msg=new messageImage(result.toString(),time,timeRef,FirebaseAuth.getInstance().getUid());
        dataRef.push().setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(cont, "message is sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(cont, "message isn't sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
