package com.example.messagingapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.messagingapp1.Adapters.fragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatActivity extends AppCompatActivity {
    TabLayout tab;
    ViewPager view;
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable d=getDrawable(R.drawable.ic_baseline_more_vert_24);
        toolbar.setOverflowIcon(d);
        tab=findViewById(R.id.tabltout);
        view=findViewById(R.id.viewPager);
        tab.setupWithViewPager(view);
        view.setAdapter(new fragmentAdapter(getSupportFragmentManager(),tab.getTabCount()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.profile){
            Intent inten=new Intent(this,profileActivity.class);
            startActivity(inten);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
      helper.setChat(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        helper.setChat(false);
    }
}