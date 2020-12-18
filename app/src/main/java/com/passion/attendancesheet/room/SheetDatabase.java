package com.passion.attendancesheet.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.passion.attendancesheet.room.dao.SheetDao;
import com.passion.attendancesheet.room.entity.Attendance;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.entity.CourseTeacherCrossRef;
import com.passion.attendancesheet.room.entity.Sheet;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.entity.Teacher;
import com.passion.attendancesheet.room.view.CourseTeacherView;
import com.passion.attendancesheet.room.view.SheetAttendanceView;
import com.passion.attendancesheet.room.view.SheetDetailView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Database( entities = { Course.class, Student.class, Teacher.class , CourseTeacherCrossRef.class, Sheet.class, Attendance.class} , views = {SheetDetailView.class , SheetAttendanceView.class, CourseTeacherView.class}, exportSchema = true,  version = 1)
public abstract  class SheetDatabase extends RoomDatabase {

    // DAO
    public abstract SheetDao sheetDao();

    // singleton database
    private static volatile SheetDatabase instance;

    static public SheetDatabase getDatabase( Context context ){
        if( instance == null ){
            synchronized ( SheetDatabase.class ){
                instance = Room.databaseBuilder( context.getApplicationContext(), SheetDatabase.class, "sheets_db")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
        }

        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AsyncPopulateDb(instance).execute();
        }
    };

    private static class AsyncPopulateDb extends AsyncTask< Void, Void, Void > {

        private SheetDao sheetDao;

        public AsyncPopulateDb( SheetDatabase sDb){
            sheetDao = sDb.sheetDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // add courses
            sheetDao.insertCourse( new Course(100001, "BCA", 5, 0) );
            sheetDao.insertCourse( new Course(100002,"BBA", 5, 0) );

            // add students
            sheetDao.insertStudent( new Student(1, "Anjali Kumari", 100001));
            sheetDao.insertStudent( new Student(2, "Anjali Jaiswal", 100001));
            sheetDao.insertStudent( new Student(3, "Anjali Gupta", 100001));
            sheetDao.insertStudent( new Student(4, "Anshita kumari", 100001));
            sheetDao.insertStudent( new Student(5, "Anshit Tomar", 100001));
            sheetDao.insertStudent( new Student(6, "Badal", 100001));
            sheetDao.insertStudent( new Student(7, "Piyush Bora", 100001));
            sheetDao.insertStudent( new Student(8, "Roshan Kumar", 100001));
            sheetDao.insertStudent( new Student(9, "Saurabh Singh", 100001));
            sheetDao.insertStudent( new Student(10, "Siddharth", 100001));
            sheetDao.insertStudent( new Student(22, "Neeraj Singh", 100002));
            sheetDao.insertStudent( new Student(121, "Manoj Kumar", 100002));
            sheetDao.insertStudent( new Student(12, "Aniket Moraya", 100002));

            // add teachers
            sheetDao.insertTeacher(new Teacher(101, "Rohit Kapoor"));
            sheetDao.insertTeacher(new Teacher( 102, "L.S Awasthi"));
            sheetDao.insertTeacher(new Teacher(103, "Santosh Kumar"));
            sheetDao.insertTeacher(new Teacher( 104, "Ajay Kumar"));
            sheetDao.insertTeacher( new Teacher( 105, "Ajay Bhajpai"));

            // add cros ref to courseWithTeacher
            sheetDao.insertCourseWithTeacherRef( new CourseTeacherCrossRef(100001, 101));
            sheetDao.insertCourseWithTeacherRef( new CourseTeacherCrossRef(100001, 102));
            sheetDao.insertCourseWithTeacherRef( new CourseTeacherCrossRef( 100001, 103));
            sheetDao.insertCourseWithTeacherRef( new CourseTeacherCrossRef( 100001, 104));
            sheetDao.insertCourseWithTeacherRef( new CourseTeacherCrossRef( 100002, 105));

            // add sheets in history
            String date_time= new SimpleDateFormat( "MMM dd, yyyy-hh:mm a").format(Calendar.getInstance().getTime());
            sheetDao.insertSheet( new Sheet( date_time, 100001, 1, "Networking", 101) );

            return null;
        }
    }
}
