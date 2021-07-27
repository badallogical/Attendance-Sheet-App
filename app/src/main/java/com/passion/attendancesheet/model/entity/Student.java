package com.passion.attendancesheet.model.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey( entity = Course.class, parentColumns = "course_and_sem", childColumns = "course_id", onDelete = CASCADE),
        primaryKeys = { "roll_number", "course_id"})
public class Student {

    public int roll_number;

    public String name;

    @NonNull
    public String course_id;

    public Student(int roll_number, String name, String course_id ){
        this.roll_number = roll_number;
        this.name = name;
        this.course_id = course_id;
    }

}
