package com.passion.attendancesheet.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.model.entity.Attendance;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;

import java.util.List;

import com.passion.attendancesheet.model.entity.Subject;
import com.passion.attendancesheet.model.entity.views.TeacherAndCoursesView;

public class AttendanceSheetViewModel extends AndroidViewModel {

    private AttendanceSheetRepository mRepository;

    public AttendanceSheetViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AttendanceSheetRepository( application );
    }

    // Insert
    public void addCourse( Course course ){
        mRepository.addCourse( course );
    }

    public void addStudent( Student student ){
        mRepository.addStudent( student );
    }

    public void addTeacher( Teacher teacher ) { mRepository.addTeacher(teacher);}

    public void addCourseTeacher(TeacherCourseCross teacherCourseCross ){
        mRepository.addCourseTeacher( teacherCourseCross );
    }

    public void addAttendanceSheet(Attendance_sheet sheet ){
        mRepository.addAttendanceSheet( sheet );
    }


    public void addAttendance(Attendance attendance ){
        mRepository.addAttendance( attendance );
    }

    public void updateAttendance(Attendance attendance){
        mRepository.updateAttendance(attendance);
    }

    public void addSubject( Subject subj ){
       mRepository.addSubject( subj );
    }

    // Fetch

    public LiveData<List<Attendance>> getStudentsAttendance( int sheet_id ){
        return mRepository.getStudentsAttendance(sheet_id);
    }

    public LiveData<List<Attendance_sheet>> getAllSheetsByCourseId( String course_id ){
        return mRepository.getAllSheetsByCourseId(course_id);
    }

    public LiveData<String> getTeacherNameById( int teacher_id ){
        return mRepository.getTeacherNameById(teacher_id );
    }

    public LiveData<String> getCourseSubject( String courseId ){
        return mRepository.getCourseSubject(courseId);
    }

    public LiveData<List<TeacherAndCoursesView>> getCourseTeacher(String courseId ){
        return mRepository.getCourseTeacher(courseId);
    }

    public LiveData<Integer> getStudentCount(String courseId ){
        return mRepository.getStudentCount(courseId);
    }

    public LiveData<List<Student>> getAllStudent(String course_id ){
        return mRepository.getAllStudent( course_id );
    }

    // DELETE

    public void removeAttendance( int sheet_id, int roll_no ){
        mRepository.removeAttendance( sheet_id, roll_no );
    }

    public void deleteSheetById( int id ){
        mRepository.deleteSheetById(id);
    }

    // UPDATE

    public void updateSheetTotalPresentAndAbsent( int sheet_id, int presents , int absents ){
        mRepository.updateSheetTotalPresentAndAbsent(sheet_id, presents, absents);
    }

    // TRANSACTION
    public void saveAttendanceSheetWithAttendance(Attendance_sheet sheet, List<Student> presents , List<Student> markedAbsent, String mode){
        mRepository.saveAttendanceSheetWithAttendance(sheet, presents, markedAbsent, mode);
    }

}
