package com.example.messagingapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class setProfileActivity extends AppCompatActivity {
    ImageView img;
    EditText txtUsrName;
    ProgressBar pg;

    Uri Path;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        img=findViewById(R.id.imgUser);
        txtUsrName=findViewById(R.id.txtuserName);

        pg=findViewById(R.id.progressBarprofile);
        storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(FirebaseAuth.getInstance().getUid()).child("profile Pic");
        firestore=FirebaseFirestore.getInstance();
    }

    public void pickImage(View view) {
        Intent inten=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(inten,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK){
            Path=data.getData();
            img.setImageURI(Path);
        }
    }

    public void setProfile(View view) {
        if (Path == null) {
            Toast.makeText(this, "Please pick Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(txtUsrName.getText())) {
            txtUsrName.setError("Empty");
        } else {
            pg.setVisibility(View.VISIBLE);
            Bitmap bitm = null;
            try {
                bitm = MediaStore.Images.Media.getBitmap(getContentResolver(), Path);
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
                            hashMap.put("name", txtUsrName.getText().toString());
                            hashMap.put("id", FirebaseAuth.getInstance().getUid());
                            hashMap.put("Urimg", uri.toString());
                            hashMap.put("Status", "online");
                            ref.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    pg.setVisibility(View.INVISIBLE);
                                    Toast.makeText(setProfileActivity.this, "your data is saved", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(setProfileActivity.this,ChatActivity.class));
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pg.setVisibility(View.INVISIBLE);
                    Toast.makeText(setProfileActivity.this, "Failed saving data Check your network", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}