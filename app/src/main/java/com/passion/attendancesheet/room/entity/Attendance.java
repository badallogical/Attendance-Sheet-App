package com.passion.attendancesheet.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity( tableName = "attendance_table", primaryKeys = { "sheet_id", "student_no", "course_id" } )
public class Attendance {

    public int sheet_id;
    public int student_no;

    @NonNull
    public String course_id;

    public Attendance( int sheet_id,  String course_id, int student_no){
        this.sheet_id = sheet_id;
        this.course_id = course_id;
        this.student_no = student_no;
    }

}
