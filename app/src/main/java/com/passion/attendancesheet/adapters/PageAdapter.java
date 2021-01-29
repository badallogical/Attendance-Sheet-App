package com.passion.attendancesheet.adapters;

import android.content.Context;

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

    Context mainContext;

    public PageAdapter(@NonNull FragmentManager fm, SheetViewModel viewModel , Context mainContext) {
        super(fm);

        this.viewModel = viewModel;
        this.sheetList = new SheetList(mainContext);
        this.history = new HistoryList();
        this.mainContext = mainContext;
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

    public SheetList getSheetList() {
        return sheetList;
    }
}
