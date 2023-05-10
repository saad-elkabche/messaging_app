package com.example.messagingapp1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class chatfrag extends Fragment {
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirestoreRecyclerAdapter<profilemodel,VH> adapter;
    RecyclerView rc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chatfrag, container, false);


         rc=v.findViewById(R.id.RCchat);
        firestore=FirebaseFirestore.getInstance();


        Query query=firestore.collection("users").whereNotEqualTo("id",FirebaseAuth.getInstance().getUid());
        FirestoreRecyclerOptions<profilemodel> options=new FirestoreRecyclerOptions.Builder<profilemodel>()
                .setQuery(query,profilemodel.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<profilemodel, VH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VH holder, int position, @NonNull profilemodel model) {
                holder.txtName.setText(model.getName());
                Glide.with(getContext())
                        .load(model.getUrimg())
                        .into(holder.img);
                if(model.getStatus().equals("online")){
                    holder.imgStatus.setImageResource(R.drawable.green);
                }
                else
                    holder.imgStatus.setImageResource(R.drawable.red);

                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent inten=new Intent(getContext(),discutionActivity.class);
                        inten.putExtra("name",model.getName());
                        inten.putExtra("img",model.getUrimg());
                        inten.putExtra("id",model.getId());
                        startActivity(inten);
                    }
                });
            }

            @NonNull
            @Override
            public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
                return new VH(view);
            }
        };
        /*rc.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);*/
        rc.setLayoutManager(new catchException(getContext(),LinearLayoutManager.VERTICAL,false));
        rc.setAdapter(adapter);
        return  v;
    }
    class VH extends RecyclerView.ViewHolder{
        TextView txtName;
        ImageView img;
        ImageView imgStatus;
        View item;
        public VH(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtusername);
            img=itemView.findViewById(R.id.imguser);
            imgStatus=itemView.findViewById(R.id.status);
            item=itemView;
            CardView card=itemView.findViewById(R.id.carpar);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View v=getLayoutInflater().inflate(R.layout.show_users_profile,null);
                    ImageView img=v.findViewById(R.id.imgusers);
                    ImageView imgSource=view.findViewById(R.id.imguser);
                    img.setImageDrawable(imgSource.getDrawable());
                    AlertDialog.Builder build=new AlertDialog.Builder(getContext());
                    build.setView(v);
                    AlertDialog dialog= build.create();
                    dialog.getWindow().setGravity(Gravity.TOP);
                    dialog.show();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null){

            adapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
    }
}