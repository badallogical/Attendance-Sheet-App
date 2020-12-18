package com.passion.attendancesheet.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity( tableName = "sheet_table")
public class Sheet {

    @PrimaryKey( autoGenerate = true )
    public int sheet_id;

    public String date;

    public int course_id;

    public int lecture;

    public String subject;

    public int teacher_id;

    public Sheet( String date, int course_id, int lecture, String subject, int teacher_id ){
        this.date = date;
        this.course_id = course_id;
        this.lecture = lecture;
        this.subject = subject;
        this.teacher_id = teacher_id;
    }
}
