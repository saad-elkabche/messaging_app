package com.example.messagingapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class setPassWordAct extends AppCompatActivity {

    EditText txtPass;
    ProgressBar pg;
    TextView txtResend;

    FirebaseAuth firebaseAuth;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass_word);

        txtPass =findViewById(R.id.txtPassword);
        pg =findViewById(R.id.progressBarSetPass);

        firebaseAuth=FirebaseAuth.getInstance();



        txtResend=findViewById(R.id.txtResend);
        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(setPassWordAct.this,MainActivity.class));
            }
        });
    }

    public void check(View view) {
        if(TextUtils.isEmpty(txtPass.getText())){
            txtPass.setError("empty");
        }
        else{
            pg.setVisibility(View.VISIBLE);
            code=getIntent().getStringExtra("pass");
            PhoneAuthCredential credential= PhoneAuthProvider.getCredential(code,txtPass.getText().toString());
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        pg.setVisibility(View.INVISIBLE);
                        Toast.makeText(setPassWordAct.this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(setPassWordAct.this,setProfileActivity.class));
                    }
                    else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(setPassWordAct.this, "password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            );
        }
    }
}