package com.passion.attendancesheet.room.view;

import androidx.room.DatabaseView;

@DatabaseView( value = "select sheet_id, student_no, student_table.name as student_name from student_table inner join attendance_table using(student_no, course_id)", viewName = "sheet_attendace_view")
public class SheetAttendanceView {
    public int sheet_id;
    public int student_no;
    public String student_name;
}
