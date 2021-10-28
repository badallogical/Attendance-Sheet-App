package com.passion.attendancesheet.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.passion.attendancesheet.model.entity.Attendance;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;
import com.passion.attendancesheet.model.entity.Subject;
import com.passion.attendancesheet.model.entity.views.TeacherAndCoursesView;

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

    @Update( onConflict = OnConflictStrategy.REPLACE)
    void updateAttendance( Attendance attendance );

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    void addSubject( Subject Subj );

    // Fetch Data

    @Query("Select * from Attendance where sheet_id = :sheet_id order by roll_no asc")
    LiveData<List<Attendance>> getStudentsAttendance( int sheet_id );

    @Query("Select * from Attendance_sheet where course_id =:course_id order by id desc")
    LiveData<List<Attendance_sheet>> getAllSheetsByCourseId( String course_id );

    @Query("Select name from teacher where id = :teacher_id ")
    LiveData<String> getTeacherNameById( int teacher_id );

    @Query("Select subjects from Subject where course_id = :courseId ")
    LiveData<String> getCourseSubject( String courseId );

    @Query("Select *  from TeacherAndCoursesView where course_id = :course_id ")
    LiveData<List<TeacherAndCoursesView>> getCourseTeachers(String course_id );

    @Query("Select strength from course where course_and_sem = :courseId")
    LiveData<Integer> getStudentCount(String courseId );

    // Fetch Live Data
    @Query("Select * from student where course_id = :courseId ")
    LiveData<List<Student>> getAllStudent( String courseId );


    @Query("Select * from teacher")
    LiveData<List<Teacher>> getAllTeacher();

    // DELETE

    @Delete
    void removeAttendance( Attendance attendance );

    @Query("Delete from Attendance_sheet where id =:id")
    void deleteSheetById( int id );




}
