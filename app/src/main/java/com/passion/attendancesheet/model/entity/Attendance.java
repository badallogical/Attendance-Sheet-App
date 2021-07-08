package com.passion.attendancesheet.model.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity( foreignKeys = @ForeignKey( entity = Attendance_sheet.class, parentColumns = "id", childColumns = "sheet_id", onDelete = CASCADE))
public class Attendance {

    @PrimaryKey( autoGenerate = true )
    @NonNull
    public int sheet_id;

    public String student_name;
    public String student_status; // Present, Absent, Bunked, Permitted

}
