package com.passion.attendancesheet.model.entity.views;


import androidx.room.DatabaseView;

@DatabaseView( "Select Teacher.teacher_id, course_id,Teacher.name as teacher_name from TeacherCourseCross INNER JOIN Teacher on TeacherCourseCross.teacher_id = Teacher.teacher_id INNER JOIN Course on course_id = Course.course_and_sem")
public class TeacherAndCoursesView {

    public long teacher_id;
    public String course_id;

    public String teacher_name;
}
