package com.passion.attendancesheet.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.model.entity.Attendance;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;
import com.passion.attendancesheet.model.entity.Subject;
import com.passion.attendancesheet.model.entity.views.TeacherAndCoursesView;

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

    void updateAttendance( Attendance attendance){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.updateAttendance( attendance );
        });
    }

    void addSubject( Subject subj ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.addSubject( subj );
        });
    }


    // Fetch Live Data

    LiveData<List<Attendance>> getStudentsAttendance( int sheet_id ){
        return sheetDao.getStudentsAttendance(sheet_id);
    }

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


    void removeAttendance( int sheet_id, int roll_no  ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.removeAttendance( sheet_id , roll_no );
        });
    }

    void deleteSheetById( int id ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.deleteSheetById(id);
        });
    }


    // UPDATE

    void updateSheetTotalPresentAndAbsent( int sheet_id, int presents , int absents ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.updateSheetTotalPresentAndAbsent(sheet_id, presents, absents );
        });
    }

    // TRANSACTION
    void saveAttendanceSheetWithAttendance(Attendance_sheet sheet, List<Student> presents, List<Student> markedAbsent, String mode ){
        AttendanceSheetDatabase.databaseWriteExecutor.execute( () -> {
            sheetDao.saveAttendanceSheetWithAttendance( sheet, presents, markedAbsent, mode);
        });
    }

}
