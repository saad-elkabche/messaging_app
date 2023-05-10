package com.example.messagingapp1;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class profileActivity extends AppCompatActivity {
    ImageView img;
    ImageView imgUpdate;
    EditText txtProfile;
    AlertDialog dialog;
    Uri path;
    TextView txtparent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        img=findViewById(R.id.imgUserprofile);
        txtparent=findViewById(R.id.txtuserName);
        //fetch data
       datachaged();
        CardView cardView=findViewById(R.id.cardProfile);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicture.d=img.getDrawable();
                startActivity(new Intent(profileActivity.this,showPicture.class));
            }
        });


    }
    void datachaged(){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        DocumentReference reference=firestore.collection("users").document(FirebaseAuth.getInstance().getUid());
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    txtparent.setText(documentSnapshot.get("name").toString());
                    Glide.with(profileActivity.this).load(documentSnapshot.get("Urimg").toString()).into(img);
                }
            }
        });
    }

    public void backToChat(View view) {
        Intent inten=new Intent(this,ChatActivity.class);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(inten);
    }

    public void updateProfile(View view) {

        AlertDialog.Builder build=new  AlertDialog.Builder(this);
        View v=getLayoutInflater().inflate(R.layout.updateprofile,null);
        imgUpdate=v.findViewById(R.id.imgUser);
        txtProfile=v.findViewById(R.id.txtuserName);
        ProgressBar pg=v.findViewById(R.id.progressBarUpdate);
        imgUpdate.setImageDrawable(img.getDrawable());
        txtProfile.setText(txtparent.getText());

        ImageButton btnback=v.findViewById(R.id.backToparent);
        //back

        //pick
        ImageButton btnPick=v.findViewById(R.id.pickImageButton);
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,123);
            }
        });
        //save
        Button btn=v.findViewById(R.id.btnSavech);
        btn.setOnClickListener((vi)->update(pg));

        build.setView(v);
        dialog= build.create();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private void update(ProgressBar pg) {

        if(TextUtils.isEmpty(txtProfile.getText())){
            txtProfile.setError("empty");
        }
        else if(path==null)
        {
            Toast.makeText(this, "pick image please", Toast.LENGTH_SHORT).show();
        }
        else{
            //UPdate profile
            pg.setVisibility(View.VISIBLE);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(FirebaseAuth.getInstance().getUid()).child("profile Pic");

            FirebaseFirestore firestore=FirebaseFirestore.getInstance();


            Bitmap bitm = null;
            try {
                bitm = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
            } catch (IOException exception) {

            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
            UploadTask task = storageReference.putBytes(bytes);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DocumentReference ref = firestore.collection("users").document(FirebaseAuth.getInstance().getUid());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", txtProfile.getText().toString());
                            hashMap.put("id", FirebaseAuth.getInstance().getUid());
                            hashMap.put("Urimg", uri.toString());
                            hashMap.put("Status", "online");
                            ref.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    pg.setVisibility(View.INVISIBLE);
                                    Toast.makeText(profileActivity.this, "your data is saved", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    datachaged();
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(profileActivity.this, "Failed saving data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123&&resultCode==RESULT_OK){
            path=data.getData();
            imgUpdate.setImageURI(data.getData());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       helper.setProf(true);
    }

    @Override
    protected void onStop() {
        helper.setProf(false);
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        Intent inten=new Intent(this,ChatActivity.class);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(inten);
    }
}