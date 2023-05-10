package com.example.messagingapp1.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagingapp1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class pictureAdapter extends RecyclerView.Adapter<pictureAdapter.VH> {
    Context cont;
    ArrayList<Drawable> list;
    ImageView MainImage;



    public void setList(ArrayList<Drawable> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public pictureAdapter(Context cont, ArrayList<Drawable> list ,ImageView img) {
        this.cont = cont;
        this.list = list;
        this.MainImage=img;

    }

    @NonNull
    @Override
    public pictureAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(cont).inflate(R.layout.picture,parent,false);
       return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull pictureAdapter.VH holder, int position) {
        holder.img.setImageDrawable(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView img;
        FloatingActionButton fab;

        
        public VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgItem);
            fab = itemView.findViewById(R.id.checkIcon);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainImage.setImageDrawable(img.getDrawable());
                }
            });
        }

    }

}
