package com.passion.attendancesheet.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "student_table")
public class Student {

    @PrimaryKey
    public int student_id;

    public String name;

    public int course_id;

    public Student( int student_id, String name, int course_id ){
        this.student_id = student_id;
        this.name = name;
        this.course_id = course_id;
    }
}
