package com.passion.attendancesheet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.passion.attendancesheet.R;

import java.util.ArrayList;

public class AdminCourseListAdapter extends RecyclerView.Adapter<AdminCourseListAdapter.CourseListViewHolder> {

    ArrayList<String> courseList;

    public AdminCourseListAdapter( ArrayList<String> courseList ){
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_course_card, parent, false );
        return new CourseListViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        holder.text.setText( courseList.get(position ));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class CourseListViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public CourseListViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.course_text);

        }
    }

}
