package com.example.messagingapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class showPicture extends AppCompatActivity {
    public static Drawable d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        ImageView img=findViewById(R.id.imgProfile);
        img.setImageDrawable(d);
    }

    public void backtopar(View view) {
        finish();
    }
}