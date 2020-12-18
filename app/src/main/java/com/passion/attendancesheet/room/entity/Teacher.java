package com.passion.attendancesheet.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity( tableName="teacher_table")
public class Teacher implements Serializable {

    @PrimaryKey
    public int teacher_id;

    public String name;

    public Teacher( int teacher_id, String name ){
        this.teacher_id = teacher_id;
        this.name = name;
    }
}
