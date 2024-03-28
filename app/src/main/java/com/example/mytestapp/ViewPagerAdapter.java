package com.example.mytestapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter  extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new Profile();
            case 1: return new Apartments();
            case 2: return new Match();
            case 3: return new Messages();
            default: return new Match();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
