package com.passion.attendancesheet.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.passion.attendancesheet.fragments.HistoryList;
import com.passion.attendancesheet.fragments.SheetList;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.room.SheetViewModel;
import com.passion.attendancesheet.room.entity.Course;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {

    SheetList sheetList;
    HistoryList history;
    SheetViewModel viewModel;

    public PageAdapter(@NonNull FragmentManager fm, SheetViewModel viewModel ) {
        super(fm);

        this.viewModel = viewModel;

        sheetList = new SheetList();
        history = new HistoryList();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return sheetList;
            case 1: return history;
        }

        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "Sheet";
            case 1: return "History";
        }

        return "";

    }

    @Override
    public int getCount() {
        return 2;
    }
}
