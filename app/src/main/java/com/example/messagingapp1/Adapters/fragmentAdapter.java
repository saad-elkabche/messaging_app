package com.example.messagingapp1.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.messagingapp1.callsfrag;
import com.example.messagingapp1.chatfrag;

import java.util.ArrayList;

public class fragmentAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> listFrag=new ArrayList<>();
    ArrayList<CharSequence> listTitles=new ArrayList<>();

    public fragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        populateFrag();
    }

    private void populateFrag() {
        listFrag.add(new chatfrag());
        listFrag.add(new callsfrag());

        listTitles.add("CHAT");
        listTitles.add("CALLS");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFrag.get(position);
    }

    @Override
    public int getCount() {
        return listFrag.size();
    }
}
