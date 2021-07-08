package com.passion.attendancesheet.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;

import java.util.List;

@Dao
public interface AttendanceSheetDao {


    // Insert Queries
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void addCourse( Course course );

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void addTeacher(Teacher teacher );

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void addCourseTeacher(TeacherCourseCross teacherCourseCross );

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void addStudent( Student student );

    // Fetch Live Data
    @Query("Select * from student where course_id = :courseId ")
    LiveData<List<Student>> getAllStudent( int courseId );

    // Fetch Data
    @Query("Select id from course where course_and_sem = :course_name;")
    int getCourseId( String course_name );




}
