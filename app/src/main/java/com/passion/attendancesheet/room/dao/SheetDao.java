package com.passion.attendancesheet.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.passion.attendancesheet.room.entity.Attendance;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.entity.CourseTeacherCrossRef;
import com.passion.attendancesheet.room.entity.Sheet;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.entity.Teacher;
import com.passion.attendancesheet.room.view.CourseTeacherView;
import com.passion.attendancesheet.room.view.SheetAttendanceView;
import com.passion.attendancesheet.room.view.SheetDetailView;

import java.util.List;

@Dao
public interface SheetDao {

   // get all the courses
   @Query("Select * from course_table")
   LiveData<List<Course>> getAllCourses();

   @Query("Select * from student_table where course_id = :course_id order by student_id")
   LiveData<List<Student>>  getAllStudents(int course_id);

   // course teachers
   @Query("Select * from course_teacher_view where course_id = :course_id")
   LiveData<List<CourseTeacherView>> getCourseTeacher( int course_id );

   /** Sheets **/
   // get all sheets
   @Query("Select * from sheet_detail_table order by sheet_id desc")
   LiveData<List<SheetDetailView>> getAllSheets();

   @Query("Select * from sheet_detail_table order by sheet_id desc limit 1")
   SheetDetailView getTopSheetsSync();

   @Query("Select * from sheet_detail_table where sheet_id = :sheet_id ")
   LiveData<List<SheetDetailView>> getSheetDetailBySheetId(int sheet_id);


   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insertSheet(Sheet s);

   @Delete
   void deleteSheet(Sheet s);

   @Query( "DELETE from sheet_table where sheet_id = :sheed_id")
   void deleteSheetById( int sheed_id );

   @Update
   void updateSheet(Sheet s);

   @Insert( onConflict = OnConflictStrategy.REPLACE)
   void insertAttendace(Attendance attendance);

   @Delete
   void deleteAttendace(Attendance attendance);

   @Query("DELETE from attendance_table where sheet_id = :sheet_id")
   void deleteAttendanceOfSheet( int sheet_id );

   // consist student list that are present on that sheet
   @Transaction
   @Query("Select * from sheet_attendace_view where sheet_id = :sheet_id ")
   LiveData<List<SheetAttendanceView>> getSheetAttendance(int sheet_id);


   // insert the course
   @Insert
   void insertCourse( Course course );

   // update the course
   @Update
   void updateCourse( Course course );

   // insert the student
   @Insert(entity = Student.class)
   void insertStudent( Student student );

   // update the student
   @Update
   void updateStudent( Student student );

   @Insert
   void insertTeacher( Teacher teacher );

   @Insert
   void insertCourseWithTeacherRef(CourseTeacherCrossRef courseTeacherCrossRef );

}
