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
public abstract class SheetDao {

   // get all the courses
   @Query("Select * from course_table")
   public abstract LiveData<List<Course>> getAllCourses();

   @Query("Select * from student_table where course_id = :course_id order by student_no")
   public abstract LiveData<List<Student>>  getAllStudents(String course_id);

   // course teachers
   @Query("Select * from course_teacher_view where course_id = :course_id")
   public abstract LiveData<List<CourseTeacherView>> getCourseTeacher(String course_id);

   @Query("Select * from teacher_table")
   public abstract LiveData<List<Teacher>> getAllTeachers();

   /** Sheets **/
   // get all sheets
   @Query("Select * from sheet_detail_table order by sheet_id desc")
   public abstract LiveData<List<SheetDetailView>> getAllSheets();

   @Query("Select * from sheet_detail_table order by sheet_id desc limit 1")
   public abstract SheetDetailView getTopSheetsSync();

   @Query("Select * from sheet_detail_table where sheet_id = :sheet_id ")
   public abstract LiveData<List<SheetDetailView>> getSheetDetailBySheetId(int sheet_id);


   @Insert(onConflict = OnConflictStrategy.REPLACE)
   public abstract void insertSheet(Sheet s);

   @Delete
   public abstract void deleteSheet(Sheet s);

   @Query( "DELETE from sheet_table where sheet_id = :sheed_id")
   public abstract void deleteSheetById(int sheed_id);

   @Update
   public abstract void updateSheet(Sheet s);

   @Insert( onConflict = OnConflictStrategy.REPLACE)
   public abstract void insertAttendace(Attendance attendance);

   @Delete
   public abstract void deleteAttendace(Attendance attendance);

   @Query("DELETE from attendance_table where sheet_id = :sheet_id")
   public abstract void deleteAttendanceOfSheet(int sheet_id);

   // consist student list that are present on that sheet
   @Transaction
   @Query("Select * from sheet_attendace_view where sheet_id = :sheet_id ")
   public abstract LiveData<List<SheetAttendanceView>> getSheetAttendance(int sheet_id);


   /** Course */
   // insert the course
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   public abstract void insertCourse(Course course);

   // update the course
   @Update
   public abstract void updateCourse(Course course);

   @Query("Delete from course_table where course_id = :courseId")
   public abstract void deleteCourse(String courseId);

   // Delete the Course Student (Course) as transaction as run multiple queries as one.
   @Transaction
   public void deleteStudentsByCourseId( String courseId ){
      deleteStudentByCourseIdHelper(courseId);
      deleteCourse(courseId);
   }


   /** Student */
   // insert the student
   @Insert(entity = Student.class , onConflict = OnConflictStrategy.REPLACE)
   public abstract void insertStudent(Student student);

   // update the student
   @Update
   public abstract void updateStudent(Student student);

   @Query("Delete from student_table where course_id = :courseId ")
   public abstract void deleteStudentByCourseIdHelper(String courseId);

   /** Teacher */
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   public abstract void insertTeacher(Teacher teacher);

   @Query("Select count(*) from teacher_table")
   public abstract int getTeachersCount();

   @Query("Delete from teacher_table")
   public abstract int deleteTeacherTable();

   /** CourseTeacherCross */

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   public abstract void insertCourseWithTeacherRef(CourseTeacherCrossRef courseTeacherCrossRef);

   @Query("Delete from courseteachercrossref")
   public abstract void deleteCourseWithTeacherRef();

}
