package com.example.messagingapp1;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class catchException extends LinearLayoutManager {
    public catchException(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }



    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        }
        catch(IndexOutOfBoundsException e){
            Log.e("catched","good");
        }

    }
}
