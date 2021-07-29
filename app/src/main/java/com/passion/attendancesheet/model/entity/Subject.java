package com.passion.attendancesheet.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subject {

    @PrimaryKey
    @NonNull
    String course_id;

    String subjects;

    public Subject( String course_id, String subjects ){
        this.course_id = course_id;
        this.subjects = subjects;
    }

}
