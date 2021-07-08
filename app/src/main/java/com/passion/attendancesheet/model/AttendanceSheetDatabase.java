package com.passion.attendancesheet.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.passion.attendancesheet.model.entity.Attendance;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database( entities = {
        Teacher.class, Course.class, Student.class, TeacherCourseCross.class, Attendance.class, Attendance_sheet.class},
        exportSchema = false,
        version = 1)
public abstract class AttendanceSheetDatabase extends RoomDatabase {

    private static volatile AttendanceSheetDatabase instance;
    public abstract AttendanceSheetDao sheetDao();
    private static final int NUMBER_OF_THREADS = 6;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AttendanceSheetDatabase getDatabase( Context context ){

        if( instance == null ){
            synchronized ( AttendanceSheetDatabase.class ){
                instance = Room.databaseBuilder( context.getApplicationContext(), AttendanceSheetDatabase.class, "sheets_db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        return instance;
    }
}
