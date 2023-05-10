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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
   EditText txtPhoneNumber;
   ProgressBar pg;
   CountryCodePicker countryCodePicker;


   FirebaseAuth firebaseAuth;
    //FirebaseUser user;
   String strPhoneNumber;

   PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtPhoneNumber=findViewById(R.id.txtNumber);
        pg=findViewById(R.id.progressBarMain);
        countryCodePicker=findViewById(R.id.ccp);


        firebaseAuth=FirebaseAuth.getInstance();
        /* user=firebaseAuth.getCurrentUser();
         if(user!=null){
             startActivity(new Intent(MainActivity.this,ChatActivity.class));
         }*/

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                pg.setVisibility(View.INVISIBLE);
                Intent inten=new Intent(MainActivity.this,setPassWordAct.class);
                inten.putExtra("pass",s);
                startActivity(inten);
            }

        };


    }

    public void SendPass(View view) {

        if(TextUtils.isEmpty(txtPhoneNumber.getText())){
            txtPhoneNumber.setError("empty");
        }
        else if(txtPhoneNumber.getText().toString().length()<9){
            txtPhoneNumber.setError("invalid number");
        }
        else{
            pg.setVisibility(View.VISIBLE);
            String Phonewithcode= countryCodePicker.getSelectedCountryCodeWithPlus()+txtPhoneNumber.getText().toString();


            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(Phonewithcode)       // Phone number to verify
                            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent inten=new Intent(this,ChatActivity.class);
            inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(inten);
        }


    }


}