package com.example.messagingapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class firstActivity extends AppCompatActivity {
    final int splashTimeOut=1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(firstActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);
    }
}