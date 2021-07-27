package com.passion.attendancesheet.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;

import org.jetbrains.annotations.Async;

import java.util.List;

public class AttendanceSheetRepository {

    AttendanceSheetDao sheetDao;


    public AttendanceSheetRepository( Application application){
        AttendanceSheetDatabase db = AttendanceSheetDatabase.getDatabase( application );
        sheetDao = db.sheetDao();
    }

    // Insert
    void addCourse( Course course ){
       AttendanceSheetDatabase.databaseWriteExecutor.execute ( () -> {
            sheetDao.addCourse(course);
        });
    }

    void addStudent( Student student ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.addStudent( student );
        });
    }

    void addTeacher( Teacher teacher ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute ( () -> {
            sheetDao.addTeacher( teacher );
        });
    }

    void addCourseTeacher(TeacherCourseCross teacherCourseCross ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.addCourseTeacher( teacherCourseCross );
        });
    }


    // Fetch Live Data
    LiveData<List<Student>> getAllStudent( String course_id ){
        return sheetDao.getAllStudent(course_id);
    }




}
