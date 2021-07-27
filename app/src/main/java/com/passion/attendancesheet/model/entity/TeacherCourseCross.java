package com.passion.attendancesheet.model.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity( foreignKeys = { @ForeignKey( entity = Teacher.class, parentColumns = "id", childColumns = "teacher_id", onDelete = CASCADE),
    @ForeignKey( entity = Course.class, parentColumns = "course_and_sem", childColumns = "course_id", onDelete = CASCADE),
}, primaryKeys = { "teacher_id", "course_id"})
public class TeacherCourseCross {

    public int teacher_id;

    @NonNull
    public String course_id;

    public TeacherCourseCross ( int teacher_id , String course_id ){
        this.teacher_id = teacher_id;
        this.course_id = course_id;
    }

}
