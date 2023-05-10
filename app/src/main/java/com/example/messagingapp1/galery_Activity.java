package com.example.messagingapp1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.messagingapp1.Adapters.pictureAdapter;
import com.example.messagingapp1.databaseStuff.message;
import com.example.messagingapp1.databaseStuff.repository;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class galery_Activity extends AppCompatActivity {
     ArrayList<Drawable> listPic=new ArrayList<>();
   ImageView img;
   RecyclerView rc;
   static Uri uri;
    repository repo;
    pictureAdapter adapter;
    String refDiali,refDialo;
    ProgressBar bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);

        refDiali=getIntent().getStringExtra("refdiali");
        refDialo=getIntent().getStringExtra("refdialo");

        rc=findViewById(R.id.RCPictures);
        img=findViewById(R.id.imgPicked);
        bar=findViewById(R.id.progressBarGalery);
        repo=new repository(getApplication());

        if(uri!=null){
            img.setImageURI(uri);
           listPic.add(img.getDrawable());
        }


       adapter=new pictureAdapter(this,listPic,img);
        LinearLayoutManager linear=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        linear.setReverseLayout(false);
        rc.setLayoutManager(linear);
        rc.setAdapter(adapter);


    }

    public void pickOtherIMage(View view) {

            Intent inten=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(inten,123);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode==RESULT_OK && requestCode==123){
              img.setImageURI( data.getData());
               listPic.add(img.getDrawable());
            }
        }

    public void deleteImage(View view) {
        if(listPic.size()<=1){
            listPic.remove(img.getDrawable());
            finish();
        }
        else {
            Drawable d=img.getDrawable();
            if(listPic.indexOf(img.getDrawable())>=1){
                img.setImageDrawable(listPic.get(listPic.indexOf(img.getDrawable())-1));
            }
          else {
                img.setImageDrawable(listPic.get(listPic.indexOf(img.getDrawable())+1));
            }
            listPic.remove(d);
        }
        adapter.setList(listPic);
    }

    public void sendPicture(View view) {
        new saveImages().execute();
    }



    class saveImages extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm");
            String time=simpleDateFormat.format(Calendar.getInstance().getTime());
            sendPicturesToFireBase sendPicturesToFireBase=new sendPicturesToFireBase(refDialo,galery_Activity.this);
            for (int i = 0; i <listPic.size() ; i++) {
                com.example.messagingapp1.databaseStuff.message msg=new message(String.valueOf(System.currentTimeMillis()),FirebaseAuth.getInstance().getUid(),refDiali,time,type.image.toString());
                mediaStuff.saveImageToInternalStorage(getApplicationContext(),msg.getMessage(),mediaStuff.convertToBitmap(listPic.get(i)),refDiali);
                repo.insertMessage(msg);
                String messageRef=String.valueOf(System.currentTimeMillis())+Integer.toString(i);
                sendPicturesToFireBase.SendPictureToFireBase(listPic.get(i),messageRef);
            }
            listPic.clear();
            return null;
        }

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            Intent inten=new Intent();
            setResult(RESULT_OK,inten);
            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}