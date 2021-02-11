package com.passion.attendancesheet.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.room.entity.Attendance;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.entity.CourseTeacherCrossRef;
import com.passion.attendancesheet.room.entity.Sheet;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.entity.Teacher;
import com.passion.attendancesheet.room.view.CourseTeacherView;
import com.passion.attendancesheet.room.view.SheetAttendanceView;
import com.passion.attendancesheet.room.view.SheetDetailView;

import java.util.ArrayList;
import java.util.List;

public class SheetViewModel extends AndroidViewModel {

    public SheetRepository sheetRepository;
    LiveData<List<Course>> allCourses;
    LiveData<List<SheetDetailView>> allSheets;

    public SheetViewModel(@NonNull Application application) {
        super(application);
        sheetRepository = new SheetRepository(application);
        allCourses = sheetRepository.getAllCourses();
        allSheets = sheetRepository.getAllSheets();
    }


    /** Course */
    public void insertCourses(Course course){
        sheetRepository.insertCourse(course);
    }

    public void deleteStudentsByCourseId( String courseId ){
        sheetRepository.deleteStudentsByCourseId( courseId );
    }

    public LiveData<List<Course>> getAllCourse(){
        return allCourses;
    }



    /** Students */
    public void insertStudents( Student student){
        sheetRepository.insertStudent(student);
    }

    public LiveData<List<Student>> getAllStudents(String course_id ){
        return sheetRepository.getAllStudent(course_id);
    }



    /** Teachers */
    public LiveData<List<Teacher>> getAllTeachers(){
        return sheetRepository.getAllTeachers();
    }

    public void insertTeacher( Teacher teacher ){
        sheetRepository.insertTeacher(teacher);
    }

    public LiveData<List<CourseTeacherView>> getTeacherCourse( String course_id ){
        return sheetRepository.getCourseTeachers(course_id);
    }

    public void deleteTeacherTable(){
        sheetRepository.deleteTeacherTable();
    }

    /** Course Sheet */
    public void insertSheet(Sheet s ){
        sheetRepository.insertSheet(s);
    }

    public void deleteSheetById( int sheet_id ){
        sheetRepository.deleteSheedById(sheet_id);
    }

    public LiveData<List<SheetDetailView>> getAllSheets(){
        return sheetRepository.getAllSheets();
    }

    public LiveData<List<SheetDetailView>> getSheetDetailBySheetId( int sheet_id ){
        return sheetRepository.getSheetDetailBySheetId(sheet_id);
    }


    /** CourseWithTeacherRef */
    public void insertCourseWithTeacherRef(CourseTeacherCrossRef courseTeacherCrossRef ){
        sheetRepository.insertCourseWithTeacherRef( courseTeacherCrossRef );
    }

    public void deleteCourseWithTeacherCrossRef(){
        sheetRepository.deleteCourseWithTeacherCrossRef();
    }



    /** Attendance Sheet */
    public void insertAttendance(Attendance attendance ){
        sheetRepository.insertAttendance(attendance);
    }

    public void deleteAttendance( Attendance attendance ){
        sheetRepository.deleteAttendance( attendance );
    }

    public void deleteSheetAttendance( int sheet_id ){
        sheetRepository.deleteSheetAttendance(sheet_id);
    }

    public LiveData<List<SheetAttendanceView>> getSheetAttendance(int sheet_id ){
        return sheetRepository.getSheetAttendance(sheet_id);
    }




}

