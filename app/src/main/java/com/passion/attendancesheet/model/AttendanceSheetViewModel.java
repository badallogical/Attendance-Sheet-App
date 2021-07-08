package com.passion.attendancesheet.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;

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

    // Fetch
    public LiveData<List<Student>> getAllStudent(int course_id ){
        return mRepository.getAllStudent( course_id );
    }

    public int getCourseId( String course_name ){
        return mRepository.getCourseId(course_name);
    }
}
