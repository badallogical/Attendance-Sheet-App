package com.passion.attendancesheet.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.passion.attendancesheet.model.entity.Attendance;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;
import com.passion.attendancesheet.model.entity.Subject;

import java.util.List;



@Dao
public interface AttendanceSheetDao {


    // Insert Queries
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void addCourse( Course course );

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void addTeacher(Teacher teacher );

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void addCourseTeacher(TeacherCourseCross teacherCourseCross );

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void addStudent( Student student );

    @Insert( onConflict =  OnConflictStrategy.REPLACE )
    void addAttendanceSheet(Attendance_sheet sheet );

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void addAttendance(Attendance attendance );

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    void addSubject( Subject Subj );

    // Fetch Data

    @Query("Select subjects from Subject where course_id = :courseId ")
    LiveData<String> getCourseSubject( String courseId );

    @Query("Select teacher_name from TeacherAndCoursesView where course_id = :course_id ")
    LiveData<List<String>> getCourseTeachers( String course_id );

    @Query("Select count(*) from student where course_id = :courseId")
    LiveData<Integer> getStudentCount(String courseId );

    // Fetch Live Data
    @Query("Select * from student where course_id = :courseId ")
    LiveData<List<Student>> getAllStudent( String courseId );


    @Query("Select * from teacher")
    LiveData<List<Teacher>> getAllTeacher();




}
