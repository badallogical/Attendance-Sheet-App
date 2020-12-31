package com.passion.attendancesheet.room.view;

import androidx.room.DatabaseView;

import java.io.Serializable;

@DatabaseView(value = "Select sheet_id, course_id , lecture, teacher_table.teacher_id, teacher_table.name as teacher_name, subject as subject_name, date from sheet_table INNER JOIN course_table using(course_id) INNER JOIN teacher_table using(teacher_id)", viewName = "sheet_detail_table")
public class SheetDetailView implements Serializable {
    public int sheet_id;
    public String course_id;
    public int lecture;
    public int teacher_id;
    public String teacher_name;
    public String subject_name;
    public String date;
}

