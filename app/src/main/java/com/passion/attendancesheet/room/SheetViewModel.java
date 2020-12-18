package com.passion.attendancesheet.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.room.entity.Attendance;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.entity.Sheet;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.view.CourseTeacherView;
import com.passion.attendancesheet.room.view.SheetAttendanceView;
import com.passion.attendancesheet.room.view.SheetDetailView;

import java.util.List;

public class SheetViewModel extends AndroidViewModel {

    SheetRepository sheetRepository;
    LiveData<List<Course>> allCourses;
    LiveData<List<SheetDetailView>> allSheets;

    public SheetViewModel(@NonNull Application application) {
        super(application);
        sheetRepository = new SheetRepository(application);
        allCourses = sheetRepository.getAllCourses();
        allSheets = sheetRepository.getAllSheets();
    }

    public void insertCourses(Course course){
        sheetRepository.insertCourse(course);
    }

    public LiveData<List<Course>> getllCourse(){
        return allCourses;
    }

    public LiveData<List<Student>> getAllStudents(int course_id ){
        return sheetRepository.getAllStudent(course_id);
    }

    public void insertStudents( Student student){
        sheetRepository.insertStudent(student);
    }

    public LiveData<List<CourseTeacherView>> getTeacherCourse( int course_id ){
        return sheetRepository.getCourseTeachers(course_id);
    }


    public void insertSheet(Sheet s ){
        sheetRepository.insertSheet(s);
    }

    public void deleteSheet( Sheet s ){
        sheetRepository.deleteSheet(s);
    }

    public void deleteSheetById( int sheet_id ){
        sheetRepository.deleteSheedById(sheet_id);
    }

    public void updateSheet( Sheet s){
        sheetRepository.updateSheet(s);
    }

    public LiveData<List<SheetDetailView>> getAllSheets(){
        return sheetRepository.getAllSheets();
    }

    public LiveData<List<SheetDetailView>> getSheetDetailBySheetId( int sheet_id ){
        return sheetRepository.getSheetDetailBySheetId(sheet_id);
    }

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

    public SheetDetailView getTopSheetSync(){
        return sheetRepository.getTopSheetsSync();
    }
}

