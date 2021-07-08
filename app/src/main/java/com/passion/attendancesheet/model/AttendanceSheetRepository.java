package com.passion.attendancesheet.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;

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


    // Fetch Live Data
    LiveData<List<Student>> getAllStudent( int course_id ){
        return sheetDao.getAllStudent(course_id);
    }

    // Fetch Data
    @Async.Execute
    int getCourseId( String course_name ){
        return sheetDao.getCourseId(course_name);
    }



}
