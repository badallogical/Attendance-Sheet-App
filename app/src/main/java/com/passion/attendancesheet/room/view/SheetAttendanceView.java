package com.passion.attendancesheet.room.view;

import androidx.room.DatabaseView;

@DatabaseView( value = "select sheet_id, student_id, student_table.name as student_name from student_table inner join attendance_table using(student_id)", viewName = "sheet_attendace_view")
public class SheetAttendanceView {
    public int sheet_id;
    public int student_id;
    public String student_name;
}
