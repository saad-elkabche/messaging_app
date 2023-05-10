package com.example.messagingapp1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagingapp1.R;
import com.example.messagingapp1.databaseStuff.message;
import com.google.firebase.auth.FirebaseAuth;

import com.example.messagingapp1.type;
import java.util.ArrayList;
import java.util.List;
import com.example.messagingapp1.mediaStuff;

public class chatAdapter extends RecyclerView.Adapter {
    List<message> list;
    Context context;
    String RefDiyali;
    public chatAdapter(List<com.example.messagingapp1.databaseStuff.message> list, Context context,String Ref) {
        this.list = list;
        this.context = context;
        this.RefDiyali=Ref;
    }

    public void setList(List<com.example.messagingapp1.databaseStuff.message> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            //ana
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ana, parent, false);
            return new VHana(view);
        } else if (viewType == 0) {
            //how
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.how, parent, false);
            return new VHhowa(v);
        } else if (viewType == -1) {
            View v = LayoutInflater.from(context).inflate(R.layout.media_ana,parent,false);
            return new VHMediaAna(v);
        }
        else {
            View v = LayoutInflater.from(context).inflate(R.layout.media_howa,parent,false);
            return new VHMediaHowa(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass() == VHana.class) {
            ((VHana) holder).txtana.setText(list.get(position).getMessage());
            ((VHana) holder).timeana.setText(list.get(position).getTime());
        } else if(holder.getClass() == VHhowa.class){
            ((VHhowa) holder).txthowa.setText(list.get(position).getMessage());
            ((VHhowa) holder).timehowa.setText(list.get(position).getTime());
        }
        else if(holder.getClass()==VHMediaAna.class){
            Bitmap bit=mediaStuff.loadImageFromInterStorage(context,list.get(position).getMessage(),RefDiyali);
            ((VHMediaAna)holder).imgAna.setImageBitmap(bit);
            ((VHMediaAna)holder).txtAna.setText(list.get(position).getTime());
        }
        else{
            Bitmap bit=mediaStuff.loadImageFromInterStorage(context,list.get(position).getMessage(),RefDiyali);
            ((VHMediaHowa)holder).imgHowa.setImageBitmap(bit);
            ((VHMediaHowa)holder).txtHowa.setText(list.get(position).getTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType=0;
        if (list.get(position).getType().equals(type.text.toString())) {
            if (list.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid()))
                viewType= 1;
            else
                viewType= 0;
        }
        else if (list.get(position).getType().equals(type.image.toString())) {

            if (list.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid()))
                viewType= -1;
            else
                viewType= -2;
        }
        return viewType;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class VHana extends RecyclerView.ViewHolder{
        TextView txtana,timeana;
        public VHana(@NonNull View itemView) {
            super(itemView);
            txtana=itemView.findViewById(R.id.txtAna);
            timeana=itemView.findViewById(R.id.timeana);
        }
    }
    class VHhowa extends RecyclerView.ViewHolder{
        TextView txthowa,timehowa;
        public VHhowa(@NonNull View itemView) {
            super(itemView);
            txthowa=itemView.findViewById(R.id.txthowa);
            timehowa=itemView.findViewById(R.id.timehowa);
        }
    }

    //Image

    class VHMediaAna extends RecyclerView.ViewHolder{
        ImageView imgAna;
        TextView txtAna;
        public VHMediaAna(@NonNull View itemView) {
            super(itemView);
            imgAna=itemView.findViewById(R.id.imageAna);
            txtAna=itemView.findViewById(R.id.timeMediaana);
        }
    }

    class VHMediaHowa extends RecyclerView.ViewHolder{
        ImageView imgHowa;
        TextView txtHowa;
        public VHMediaHowa(@NonNull View itemView) {
            super(itemView);
            imgHowa=itemView.findViewById(R.id.imagehowa);
            txtHowa=itemView.findViewById(R.id.timeMediahowa);
        }
    }

}
