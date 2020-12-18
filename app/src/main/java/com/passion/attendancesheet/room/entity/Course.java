package com.passion.attendancesheet.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity( tableName = "course_table")
public class Course implements Serializable {

    @PrimaryKey
    public int course_id;

    @NonNull
    public String name;

    public int semester;

    public int admin_teacher;

    public Course( int course_id, String name, int semester, int admin_teacher ){
        this.course_id = course_id;
        this.name = name;
        this.semester = semester;
        this.admin_teacher = admin_teacher;
    }

}
