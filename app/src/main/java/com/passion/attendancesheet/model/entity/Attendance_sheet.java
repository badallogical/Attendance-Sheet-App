package com.passion.attendancesheet.model.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Represent the attendance sheet
@Entity( foreignKeys = @ForeignKey( entity = Teacher.class, parentColumns = "id", childColumns = "teacher_id", onDelete = CASCADE))
public class Attendance_sheet {

    @PrimaryKey( autoGenerate = true )
    @NonNull
    public int id;

    public String course_id;

    public String data_and_time;

    public int lecture;

    public int teacher_id;

    public String subject;

}
