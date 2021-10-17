package com.passion.attendancesheet.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DashboardAdapter extends FragmentStateAdapter {


    public DashboardAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch ( position ){
            case 0 : return new Home();

            case 1 : return new History();
        }

        return new Home();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
