package com.passion.attendancesheet.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.view.SheetAttendanceView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class StudentListAdapter extends RecyclerView.Adapter< StudentListAdapter.StudentListViewHolder> {

    List<Student> students = new ArrayList<>();
    List<SheetAttendanceView> studentPresents = new ArrayList<>();
    Map<Integer, Boolean> studentPresentMap = new HashMap<>();
    public static HashSet<Integer> studentPresentIndex = new HashSet<Integer>();

    Context context;
    static String mode;
    static int tempOldPresentCount = 0;

    public StudentListAdapter( Context context , String _mode ){
        this.context = context;
        mode = _mode;
        tempOldPresentCount = 0;

        // reset
        studentPresentIndex.clear();
    }

    public void showExistingSheetWithEdit( List<Student> students, List<SheetAttendanceView> studentPresents){
        this.studentPresents = studentPresents;
        this.students = students;
        studentPresentIndex.clear();
        tempOldPresentCount = 0;
        notifyDataSetChanged();
    }

    @Override
    public StudentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        return new StudentListViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListViewHolder holder, int position) {
        if( mode.equals(context.getResources().getString(R.string.normal))) {
            Student s = students.get(position);

            holder.id.setText(String.valueOf(s.student_id));
            holder.name.setText(s.name);
        }
        else if( mode.equals(context.getResources().getString(R.string.edit))){
            SheetAttendanceView s = studentPresents.get(position);
            holder.id.setText( String.valueOf(s.student_id) );
            holder.name.setText( s.student_name );

            if( !studentPresentMap.containsKey( s.student_id )){
                studentPresentMap.put( s.student_id, true );
            }
        }
        else{
            // editToNormal
            Student s = students.get(position);
            holder.id.setText(String.valueOf(s.student_id));
            holder.name.setText( s.name);
            holder.setToggleButton(true);

            if( studentPresentMap.containsKey( s.student_id )){
                holder.markPresent();
                studentPresentIndex.add( position );
            }
        }

    }

    public List<SheetAttendanceView> getStudentPresents() {
        return studentPresents;
    }

    @Override
    public int getItemCount() {
        int s = 0;
        if( mode.equals(context.getResources().getString(R.string.normal)) || mode.equals(context.getResources().getString(R.string.editToNormal)))
            s = students.size();
        else
            s = studentPresents.size();

        return s;
    }

    static public void setMode( String _mode ){
        mode = _mode;
    }

    public void setStudents( List<Student> students ){
        this.students = students;
        notifyDataSetChanged();
    }

    public void setStudentPresent(List<SheetAttendanceView> studentPresents){
        this.studentPresents = studentPresents;
        notifyDataSetChanged();
    }


    public List<Student> getStudents() {
        return students;
    }

    public static class StudentListViewHolder extends RecyclerView.ViewHolder{

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

            if( mode.equals(context.getResources().getString(R.string.edit))){
                toggleButton.setVisibility(View.GONE);
                itemView.setPadding(10,10,10,10);
                itemView.setElevation(1.0f);
            }
            else if( mode.equals(context.getResources().getString(R.string.normal)) || mode.equals( context.getResources().getString(R.string.editToNormal))){
                toggleButton.setVisibility(View.VISIBLE);
            }

            presentMark = itemView.findViewById(R.id.present_dot);

            toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    presentMark.setVisibility(View.VISIBLE);
                    studentPresentIndex.add( getAdapterPosition() );
                }
                else{
                    presentMark.setVisibility(View.GONE);
                    studentPresentIndex.remove( getAdapterPosition() );
                }
            });
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

        public void setToggleButton(Boolean bool ){
            if( bool )
                toggleButton.setVisibility(View.VISIBLE);
            else
                toggleButton.setVisibility(View.GONE);
        }
    }
}
