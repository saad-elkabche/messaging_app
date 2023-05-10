package com.example.messagingapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.messagingapp1.Adapters.chatAdapter;
import com.example.messagingapp1.databaseStuff.repository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class discutionActivity extends AppCompatActivity {
    ImageView img;
    TextView txtName;
    EditText txtMessage;
    String idhow,refduali,refdualo;
    AlertDialog alert;

    boolean okeyStorage=true;
    repository rep;

    DatabaseReference ref;
    DatabaseReference ref1;

    FirebaseDatabase firebaseDatabase;
   // ArrayList<com.example.messagingapp1.databaseStuff.message> list=new ArrayList<>();
    chatAdapter adapter;
    RecyclerView rc;

    boolean okey=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discution);

        rep=new repository(getApplication());

        img=findViewById(R.id.imgUser);
        txtName=findViewById(R.id.txtNamerec);

        txtName.setText(getIntent().getStringExtra("name"));
        String uri=getIntent().getStringExtra("img");
        Glide.with(this)
                .load(uri)
                .into(img);

        idhow=getIntent().getStringExtra("id");

        txtMessage=findViewById(R.id.txtMessage);

        refduali= FirebaseAuth.getInstance().getUid()+idhow;
        refdualo=idhow+FirebaseAuth.getInstance().getUid();

        adapter=new chatAdapter(rep.getAllMessages(refduali),this,refduali);
        rc=findViewById(R.id.RCChat);
        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(this));

        //show messages
         ref=FirebaseDatabase.getInstance().getReference().child("chats").child(refduali);
         ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //list.clear();
                if(okey){
                    for(DataSnapshot snap:snapshot.getChildren()){
                        message m=snap.getValue(message.class);
                        com.example.messagingapp1.databaseStuff.message mess=new com.example.messagingapp1.databaseStuff.message(m.message,m.senderid,refduali,m.time,"message");
                        rep.insertMessage(mess);
                    }
                    deleteData();
                    //FirebaseDatabase.getInstance().getReference().child("chats").getRef().removeValue();
                    showMessages();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         ref1=FirebaseDatabase.getInstance().getReference().child("chat_Images").child(refduali);

         ref1.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (okeyStorage) {
                     new loadMessages(snapshot).execute();
                    // showMessages();
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });


        showMessages();
    }

    private void deleteData() {
       okey=false;
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                okey=true;
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
       helper.setDisc(false);
       adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper.setDisc(true);
        adapter.notifyDataSetChanged();
    }

    public void sendMessage(View view) {
        if(txtMessage.getText().toString().isEmpty()){
            Toast.makeText(this, "enter a message", Toast.LENGTH_SHORT).show();
        }
        else {
            String msg=txtMessage.getText().toString();
            txtMessage.setText("");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm");
            Calendar cal=Calendar.getInstance();
            String time=simpleDateFormat.format(cal.getTime());

            message msga=new message(msg,time,FirebaseAuth.getInstance().getUid());
            firebaseDatabase=FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("chats")
                    .child(refdualo)
                    .push()
                    .setValue(msga).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            com.example.messagingapp1.databaseStuff.message mess=new com.example.messagingapp1.databaseStuff.message(msga.message,msga.senderid,refduali,msga.time,type.text.toString());
                            rep.insertMessage(mess);
                            showMessages();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        Intent inten=new Intent(this,ChatActivity.class);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(inten);
    }

    public void backtocht(View view) {
        Intent inten=new Intent(this,ChatActivity.class);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(inten);
    }

    void showMessages(){

        adapter.setList(rep.getAllMessages(refduali));
        rc.scrollToPosition(rep.getAllMessages(refduali).size()-1);
    }

    public void showFloatingMenu(View view) {
        hideKeyBoard();

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        View v=getLayoutInflater().inflate(R.layout.floating_menu,null);

        ImageButton cardImage=v.findViewById(R.id.pickImage);
        CardView cardLocation=v.findViewById(R.id.btnLocation);
        CardView cardCamera=v.findViewById(R.id.btnCamera);
        CardView cardAudio=v.findViewById(R.id.btnAudio);

        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        }) ;
        cardLocation.setOnClickListener( (vie)-> sendLocation()) ;
        cardCamera.setOnClickListener( (vie)-> openCamera()) ;
        cardAudio.setOnClickListener( (vie)-> sendAudio()) ;

        builder.setView(v);
        alert=builder.create();

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().getAttributes().y=(int)txtMessage.getTop()*80;
        alert.show();
    }

    private void sendAudio() {
    }

    private void openCamera() {
    }

    private void sendLocation() {
    }

    private void pickImage() {
        Intent inten=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(inten,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==123){
            Intent intent=new Intent(this,galery_Activity.class);
            intent.putExtra("refdiali",refduali);
            intent.putExtra("refdialo",refdualo);
            galery_Activity.uri=data.getData();
            startActivityForResult(intent,111);
        }
        if(resultCode==RESULT_OK && requestCode==111)
        {
            alert.dismiss();
            showMessages();
        }

    }

    private void hideKeyBoard() {
        InputMethodManager manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(txtMessage.getApplicationWindowToken(),0);
    }

    void DeleteFromRealTime(DataSnapshot snapshot) {
        okeyStorage = false;
     snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
         public void onComplete(@NonNull Task<Void> task) {
             okeyStorage=true;
         }
     });
    }
    private void deletefromStorage(ArrayList<String> list) {
        StorageReference reference=FirebaseStorage.getInstance().getReference().child("chat_Images").child(refduali);
        for(String url:list){
            reference.child(url).child(url+".png").delete();
        }
    }

    class loadMessages extends AsyncTask<Void,Void,Void>{
        DataSnapshot data;
        ArrayList<String> listRefs=new ArrayList<>();

        public loadMessages(DataSnapshot data) {
            this.data = data;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            com.example.messagingapp1.databaseStuff.message messageLocal;
           //okeyStorage=false;
            for(DataSnapshot snap:data.getChildren()){

                messageImage msg=snap.getValue(messageImage.class);
                messageLocal=new com.example.messagingapp1.databaseStuff.message(String.valueOf(System.currentTimeMillis()),msg.getSenderId(),refduali,msg.getTime(),type.image.toString());
                listRefs.add(msg.getImageRef());

                try {

                    URL url=new URL(msg.getUrl());
                    Bitmap bit= BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    mediaStuff.saveImageToInternalStorage(discutionActivity.this,messageLocal.getMessage(),bit,refduali);
                    rep.insertMessage(messageLocal);
                   DeleteFromRealTime(snap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            showMessages();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            showMessages();
           deletefromStorage(listRefs);
        }
    }




}