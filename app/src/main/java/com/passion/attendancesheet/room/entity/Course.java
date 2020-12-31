package com.passion.attendancesheet.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity( tableName = "course_table")
public class Course implements Serializable {

    @NonNull
    @PrimaryKey
    public String course_id;

    public Course( @NonNull String course_id ){
        this.course_id = course_id;
    }

}
