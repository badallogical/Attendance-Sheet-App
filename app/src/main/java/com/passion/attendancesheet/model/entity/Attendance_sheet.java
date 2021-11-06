package com.passion.attendancesheet.model.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Represent the attendance sheet
@Entity
public class Attendance_sheet {

    @PrimaryKey( autoGenerate = true )
    public int id;

    public String course_id;

    public String data_and_time;

    public int lecture;

    @Embedded
    public Teacher teacher;

    public String subject;

    public int presents;

    public int absents;

    public Attendance_sheet(  String course_id, String data_and_time, int lecture, Teacher teacher, String subject ){
        this.course_id = course_id;
        this.data_and_time = data_and_time;
        this.lecture = lecture;
        this.teacher = teacher;
        this.subject = subject;
    }

}
