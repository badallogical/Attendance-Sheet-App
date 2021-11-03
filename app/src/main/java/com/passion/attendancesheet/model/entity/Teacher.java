package com.passion.attendancesheet.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Teacher {

    @PrimaryKey
    @NonNull
    public int teacher_id;

    public String name;

    public Teacher(int teacher_id, String name ){
        this.teacher_id = teacher_id;
        this.name = name;
    }

    @Override
    public String toString() {
        return teacher_id + "," + name;
    }
}
