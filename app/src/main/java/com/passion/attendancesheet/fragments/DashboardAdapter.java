package com.passion.attendancesheet.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DashboardAdapter extends FragmentStateAdapter {

    String courseId;
    int strength;

    public DashboardAdapter(@NonNull Fragment fragment, String courseId, int strength) {
        super(fragment);

        this.courseId = courseId;
        this.strength = strength;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch ( position ){
            case 0 : return new Home(courseId, strength);

            case 1 : return new History();
        }

        return new Home(courseId,strength);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
