package com.passion.attendancesheet.room.view;

import androidx.room.DatabaseView;

import java.io.Serializable;

@DatabaseView( value = "Select teacher_id, teacher_table.name as teacher_name, course_id  from teacher_table INNER JOIN CourseTeacherCrossRef using( teacher_id )", viewName = "course_teacher_view")
public class CourseTeacherView implements Serializable {
    public int teacher_id;
    public String teacher_name;
    public int course_id;
}
