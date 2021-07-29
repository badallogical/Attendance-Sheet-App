package com.passion.attendancesheet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.model.entity.Student;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentListViewHolder>{

    Context context;
    List<Student> students;

    public StudentListAdapter( Context context, List<Student> students ){
        this.context  = context;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        return new StudentListViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListViewHolder holder, int position) {
        Student s = students.get(position);
        holder.id.setText(String.valueOf(s.roll_number));
        holder.name.setText(s.name);
    }

    @Override
    public int getItemCount() {

        return students.size();

    }

    public void setStudents( List<Student> students ){
        this.students = students;
        notifyDataSetChanged();
    }

    class StudentListViewHolder extends RecyclerView.ViewHolder{



        TextView id;
        TextView name;
        ToggleButton toggleButton;
        Context context;
        ImageView presentMark;


        public StudentListViewHolder(@NonNull View itemView, Context context ) {
            super(itemView);
            id = itemView.findViewById(R.id.student_id);
            name = itemView.findViewById(R.id.student_name);
            toggleButton = itemView.findViewById(R.id.toggler);
            this.context = context;

        }




        public TextView getId() {
            return id;
        }

        public TextView getName() {
            return name;
        }

        public ToggleButton getToggleButton() {
            return toggleButton;
        }

        public void markPresent(){
            toggleButton.setChecked(true);
            toggleButton.setVisibility(View.VISIBLE);
            presentMark.setVisibility(View.VISIBLE);
        }

        public void markAbsent(){
            toggleButton.setChecked(false);
            toggleButton.setVisibility(View.VISIBLE);
            presentMark.setVisibility(View.GONE);
        }

        public void setToggleButton(Boolean bool ){
            if( bool )
                toggleButton.setVisibility(View.VISIBLE);
            else
                toggleButton.setVisibility(View.GONE);
        }
    }
}
