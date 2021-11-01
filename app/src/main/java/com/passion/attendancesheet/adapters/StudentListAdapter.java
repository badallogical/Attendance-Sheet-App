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

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentListViewHolder>{

    Context context;
    List<Student> students;         // Total Students
    List<Student> student_present;      // Present Student ONLY
    String mode;
    List<Student> student_marked_absent;

    public StudentListAdapter( Context context, List<Student> students ){
        this.context  = context;
        this.students = students;

        student_present = new ArrayList<Student>();
        student_marked_absent = new ArrayList<Student>();
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

        if( mode.equals(context.getString(R.string.normal)) || mode.equals(context.getString(R.string.editToNormal))) {

            if (student_present.contains(students.get(position))) {
                holder.markPresent();
                Timber.d("already marked present : " + position);
            } else {
                holder.markAbsent();
                Timber.d(" " + position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    public void setStudentsPresentAsList( List<Student> students_present , String mode ){
        this.mode = mode;

        if( mode.equals( context.getString(R.string.edit ))){
            this.students = students_present;
            Timber.d("students presents set to students, student present is " + students_present.size());
        }
//        else if( mode.equals( context.getString(R.string.editToNormal))){
//            this.student_present = students_present;
//            Timber.d("student present setted , student present is " + students_present.size());
//            for( Student s : students_present){
//                Timber.d(s.name);
//            }
//        }

        notifyDataSetChanged();


    }

    public List<Student> getStudentPresent() {
        return student_present;
    }

    public List<Student> getStudentMarkedAbsent(){ return student_marked_absent; }

    public void setStudents(List<Student> students , String mode){
        this.students = students;
        this.mode = mode;
        notifyDataSetChanged();
    }

    public List<Student> getStudents(){
        return students;
    }

    public void setMode( String mode ){
        this.mode = mode;
    }

    public void setStudentsAndPresent( List<Student> studentList){
        student_present.clear();
        this.student_present.addAll(this.students);
        this.students = studentList;

        Timber.d("mode - " + this.mode);

        for( Student student : student_present ){
            Timber.d("present - %s", student.name );
        }

        for( Student student : studentList ){
            Timber.d("student - %s", student.name);
        }

        if( student_present.get(0).equals( studentList.get(0)) ){
            Timber.d("student are same");
        }
        else{
            Timber.d("student not same");
        }
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
            this.context = context;

            // Get the views
            id = itemView.findViewById(R.id.student_id);
            name = itemView.findViewById(R.id.student_name);
            toggleButton = itemView.findViewById(R.id.toggler);
            presentMark = itemView.findViewById(R.id.present_dot);

            // Disable Toggle button if Edit Screen
            if( mode.equals( context.getString(R.string.edit))){
                toggleButton.setVisibility(View.GONE);
            }


            // Set the action on toggle
            toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {

                Timber.d("Called Called HA HA HA");
                if( !isChecked ) {
                    // NOT CHECK
                    presentMark.setVisibility(View.GONE);

                    if( student_present.contains( students.get( getAdapterPosition() )) ){
                        student_present.remove(students.get(getAdapterPosition()));
                        Timber.d("Adapter position " + getAdapterPosition() + " removed ");

                        if( mode.equals(context.getString(R.string.editToNormal))){
                            student_marked_absent.add( students.get(getAdapterPosition()) );
                        }
                    }

                } else{
                    // CHECK
                    presentMark.setVisibility(View.VISIBLE);

                    if( student_present.contains( students.get( getAdapterPosition() ))  == false  ) {
                        Timber.d("Adapter position " + getAdapterPosition() + " added ");
                        student_present.add(students.get(getAdapterPosition()));
                    }

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
            toggleButton.setVisibility(View.VISIBLE);
            toggleButton.setChecked(true);
            presentMark.setVisibility(View.VISIBLE);
        }

        public void markAbsent(){
            toggleButton.setVisibility(View.VISIBLE);
            toggleButton.setChecked(false);
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
