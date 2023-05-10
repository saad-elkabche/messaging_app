package com.example.messagingapp1;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class helper {
    static boolean chat;
    static boolean prof;
    static boolean disc;

    public static void setChat(boolean chat) {
        helper.chat = chat;
        if (!chat) {
            if(!prof && !disc){
                DocumentReference ref= FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
                ref.update("Status","offline");
                return;
            }
        }
        DocumentReference ref= FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        ref.update("Status","online");

    }

    public static void setProf(boolean prof) {
        helper.prof = prof;
        if(!prof){
            if(!chat){
                DocumentReference ref= FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
                ref.update("Status","offline");
                return;
            }
        }
        DocumentReference ref= FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        ref.update("Status","online");
    }

    public static void setDisc(boolean disc) {
        helper.disc = disc;
        if(!disc){
            if(!chat){
                DocumentReference ref= FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
                ref.update("Status","offline");
                return;
            }
        }
        DocumentReference ref= FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        ref.update("Status","online");
    }
}
