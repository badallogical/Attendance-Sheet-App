package com.passion.attendancesheet.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.passion.attendancesheet.MainActivity;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.SheetListAdapter;
import com.passion.attendancesheet.room.SheetViewModel;

public class SheetList extends Fragment {

    RecyclerView  sheetlist;
    SheetListAdapter sheetListAdapter;
    SheetViewModel sheetViewModel;

    LinearLayout no_classes;

    Context context;

    MainActivity mainActivity;

    public SheetList() {
        // Required empty public constructor
    }

    public SheetList(Context mainActivity ){

        // debug
        Log.i(getClass().getName(), "sheet list constructor" + mainActivity);
        this.mainActivity = (MainActivity) mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        sheetViewModel = ViewModelProviders.of(this).get(SheetViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sheet_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // debug
        Log.i(context.getClass().getName(), "OnViewCreated");

        no_classes = getView().findViewById(R.id.no_classes);
        sheetlist = getView().findViewById( R.id.sheet_list );
        sheetListAdapter = new SheetListAdapter( context, SheetList.this );
        sheetlist.setAdapter( sheetListAdapter );

        sheetlist.setLayoutManager( new LinearLayoutManager(context));
        if( sheetViewModel.getAllCourse() != null ){
            sheetViewModel.getAllCourse().observe( this, (courses)-> {
                if( courses.isEmpty() ){
                    no_classes.setVisibility(View.VISIBLE);
                    sheetlist.setVisibility(View.GONE);
                }
                else{
                    sheetListAdapter.setCourseList(courses);
                    no_classes.setVisibility(View.GONE);
                    sheetlist.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public SheetViewModel getSheetViewModel() {
        return sheetViewModel;
    }

    public MainActivity getMainActivity(){
        return mainActivity;
    }

    public SheetListAdapter getSheetListAdapter() {
        return sheetListAdapter;
    }
}
