package com.passion.attendancesheet.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "student_table", primaryKeys = {"student_no", "course_id"})
public class Student {

    @NonNull
    public String course_id;

    public int student_no;

    public String name;



    public Student( String course_id, int student_no, String name ){
        this.course_id = course_id;
        this.student_no = student_no;
        this.name = name;
    }
}
