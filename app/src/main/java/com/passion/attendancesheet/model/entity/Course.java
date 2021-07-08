package com.passion.attendancesheet.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Course {

    @PrimaryKey( autoGenerate = true )
    @NonNull
    public int id;

    public String course_and_sem;

    public int strength;

    public Course( String course_and_sem, int strength ){
        this.course_and_sem = course_and_sem;
        this.strength = strength;
    }

}


