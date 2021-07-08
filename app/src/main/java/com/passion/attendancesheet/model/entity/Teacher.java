package com.passion.attendancesheet.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Teacher {

    @PrimaryKey
    @NonNull
    public int id;

    public String name;
}
