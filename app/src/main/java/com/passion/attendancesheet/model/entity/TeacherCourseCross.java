package com.passion.attendancesheet.model.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity( foreignKeys = { @ForeignKey( entity = Teacher.class, parentColumns = "id", childColumns = "teacher_id", onDelete = CASCADE),
    @ForeignKey( entity = Course.class, parentColumns = "id", childColumns = "course_id", onDelete = CASCADE),
}, primaryKeys = { "teacher_id", "course_id"})
public class TeacherCourseCross {

    public int teacher_id;
    public int course_id;

}
