package com.passion.attendancesheet.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.room.view.CourseTeacherView;

import java.util.ArrayList;
import java.util.List;

public class TeacherListAdapter extends ArrayAdapter<CourseTeacherView> {

    List<CourseTeacherView> teachers = new ArrayList<>();


    public TeacherListAdapter(Context context) {
        super(context, 0);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_layout,parent, false );
            convertView.setTag( teachers.get(position) );
        }

        TextView teacher_name = convertView.findViewById(R.id.text);
        teacher_name.setGravity(Gravity.CENTER);
        teacher_name.setText( teachers.get(position).teacher_name );

        return  convertView;
    }

    @Override
    public int getCount() {
        return teachers.size();
    }

    @Nullable
    @Override
    public CourseTeacherView getItem(int position) {
        return teachers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return teachers.get(position).teacher_id;
    }

    public void setTeachers(List<CourseTeacherView> teachers ){
        this.teachers = teachers;
        notifyDataSetChanged();
    }


}
