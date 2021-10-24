package com.passion.attendancesheet.model.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity( primaryKeys = {"sheet_id", "roll_no"}, foreignKeys = @ForeignKey( entity = Attendance_sheet.class, parentColumns = "id", childColumns = "sheet_id", onDelete = CASCADE))
public class Attendance {

    public int sheet_id;

    public int roll_no;
    public String student_name;
    public String student_status; // Present, Absent, Bunked, Permitted

    public Attendance( int sheet_id, int roll_no, String student_name, String student_status  ){
        this.sheet_id = sheet_id;
        this.roll_no = roll_no;
        this.student_name = student_name;
        this.student_status = student_status;
    }

}
