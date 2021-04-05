package com.passion.attendancesheet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.passion.attendancesheet.databinding.FragmentSignUpBinding;
import com.passion.attendancesheet.dataclasses.ClassRepresentative;
import com.passion.attendancesheet.dataclasses.CourseF;
import com.passion.attendancesheet.dataclasses.StudentF;

import java.util.ArrayList;

public class Sign_up extends Fragment {

    private FragmentSignUpBinding binding;

    private FirebaseDatabase database;

    public Sign_up() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Realtime Database
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("courses");


        ArrayList<CourseF> courses;
        ArrayList<ClassRepresentative> crs;
        ArrayList<StudentF> students;
        ArrayList<String> teachers;

        // BCA-6

        // Crs
        crs = new ArrayList<ClassRepresentative>();
        crs.add( new ClassRepresentative("Badal", "badallearn@gmail.com", "BCA-6"));
        crs.add( new ClassRepresentative("Shubhum Kumar", "shubhumkumar@gmail.com", "BCA-6"));


        // Students
        students = new ArrayList<StudentF>();
        students.add( new StudentF("Badal",1));
        students.add( new StudentF("Badal",2));
        students.add( new StudentF("Badal",3));
        students.add( new StudentF("Badal",4));
        students.add( new StudentF("Badal",5));
        students.add( new StudentF("Badal",6));
        students.add( new StudentF("Badal",7));
        students.add( new StudentF("Badal",8));
        students.add( new StudentF("Badal",9));
        students.add( new StudentF("Badal",10));
        students.add( new StudentF("Badal",11));
        students.add( new StudentF("Badal",12));
        students.add( new StudentF("Badal",13));


        // teachers
        teachers = new ArrayList<String>();
        teachers.add( "Rohit Sir");
        teachers.add( "Santosh Sir");
        teachers.add( "Ajay Sir");
        teachers.add( "L.S Awasthi Sir");

        // add BCA-6
        courses = new ArrayList<CourseF>();
        courses.add( new CourseF("BCA-6", 94, crs, students,teachers));

        //save course to db
        myRef.setValue(courses);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate( getLayoutInflater() );

        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}