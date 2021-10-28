package com.passion.attendancesheet.model.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(foreignKeys = @ForeignKey( entity = Course.class, parentColumns = "course_and_sem", childColumns = "course_id", onDelete = CASCADE),
        primaryKeys = { "roll_number", "course_id"})
public class Student {

    public int roll_number;

    public String name;

    @NonNull
    public String course_id;

    public Student(int roll_number, String name, String course_id ){
        this.roll_number = roll_number;
        this.name = name;
        this.course_id = course_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return roll_number == student.roll_number && name.equals(student.name) && course_id.equals(student.course_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roll_number, name, course_id);
    }
}
