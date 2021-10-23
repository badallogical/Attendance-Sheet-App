package com.passion.attendancesheet.model.entity.views;


import androidx.room.DatabaseView;

@DatabaseView( "Select teacher_id, course_id, Course.strength as course_strength,  Teacher.name as teacher_name from TeacherCourseCross INNER JOIN Teacher on teacher_id = Teacher.id INNER JOIN Course on course_id = Course.course_and_sem")
public class TeacherAndCoursesView {

    public long teacher_id;
    public String course_id;
    public long course_strength;

    public String teacher_name;
}
