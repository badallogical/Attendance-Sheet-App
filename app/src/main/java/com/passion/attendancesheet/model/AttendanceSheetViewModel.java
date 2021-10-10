package com.passion.attendancesheet.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;

import java.util.List;

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

    // Fetch

    public LiveData<Integer> getStudentCount(String courseId ){
        return mRepository.getStudentCount(courseId);
    }

    public LiveData<List<Student>> getAllStudent(String course_id ){
        return mRepository.getAllStudent( course_id );
    }

}
