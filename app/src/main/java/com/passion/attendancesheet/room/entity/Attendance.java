package com.passion.attendancesheet.room.entity;

import androidx.room.Entity;

@Entity( tableName = "attendance_table", primaryKeys = { "sheet_id", "student_id" } )
public class Attendance {

    public int sheet_id;
    public int student_id;

    public Attendance( int sheet_id, int student_id ){
        this.sheet_id = sheet_id;
        this.student_id = student_id;
    }

}
