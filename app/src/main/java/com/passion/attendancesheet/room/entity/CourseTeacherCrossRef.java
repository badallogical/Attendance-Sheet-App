package com.passion.attendancesheet.room.entity;


import androidx.room.Entity;

@Entity( primaryKeys = {"course_id", "teacher_id"} )
public class CourseTeacherCrossRef {

    public int course_id;
    public int teacher_id;

    public CourseTeacherCrossRef( int course_id , int teacher_id ){
        this.course_id = course_id;
        this.teacher_id = teacher_id;
    }

}
