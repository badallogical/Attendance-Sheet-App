package com.passion.attendancesheet.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.model.entity.Attendance;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;
import com.passion.attendancesheet.model.entity.Subject;
import com.passion.attendancesheet.model.entity.views.TeacherAndCoursesView;

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

    void addAttendanceSheet(Attendance_sheet sheet ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.addAttendanceSheet( sheet );

        });
    }

    void addAttendance( Attendance attendance ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.addAttendance( attendance );
        });
    }

    void addSubject( Subject subj ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.addSubject( subj );
        });
    }


    // Fetch Live Data

    LiveData<List<Attendance_sheet>> getAllSheetsByCourseId( String course_id ){
        return sheetDao.getAllSheetsByCourseId(course_id);
    }

    LiveData<String> getTeacherNameById( int teacher_id ){
        return sheetDao.getTeacherNameById(teacher_id);
    }

    LiveData<String> getCourseSubject( String courseId ){
        return sheetDao.getCourseSubject(courseId);
    }

    LiveData<List<TeacherAndCoursesView>> getCourseTeacher(String courseId ){
        return sheetDao.getCourseTeachers(courseId);
    }

    LiveData<Integer> getStudentCount(String courseId ){
        return sheetDao.getStudentCount(courseId);
    }


    LiveData<List<Student>> getAllStudent( String course_id ){
        return sheetDao.getAllStudent(course_id);
    }

    // DELETE

    void deleteSheetById( int id ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.deleteSheetById(id);
        });
    }


}
