package com.passion.attendancesheet.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.SheetListAdapter;
import com.passion.attendancesheet.room.SheetViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SheetList extends Fragment {

    RecyclerView  sheetlist;
    SheetListAdapter sheetListAdapter;
    SheetViewModel sheetViewModel;

    Context context;

    public SheetList() {
        // Required empty public constructor
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

//        TeacherListAdapter teacherListAdapter = new TeacherListAdapter(context);

        sheetlist = getView().findViewById( R.id.sheet_list );
        sheetListAdapter = new SheetListAdapter( context, SheetList.this );
        sheetlist.setAdapter( sheetListAdapter );
        sheetlist.setLayoutManager( new LinearLayoutManager(context));
        sheetViewModel.getllCourse().observe( this, (courses)-> {
            sheetListAdapter.setCourseList(courses);
        });


//        sheetViewModel.getTeacherFromCourse().observe(this, new Observer<List<CourseWithTeacher>>() {
//            @Override
//            public void onChanged(List<CourseWithTeacher> courseWithTeachers) {
//                teacherListAdapter.setTeachers( courseWithTeachers.get(0).teachers );
//            }
//        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(sheetlist);


    }

    public SheetViewModel getSheetViewModel() {
        return sheetViewModel;
    }
}
