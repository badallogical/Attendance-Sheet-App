package com.passion.attendancesheet.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.passion.attendancesheet.room.dao.SheetDao;
import com.passion.attendancesheet.room.entity.Attendance;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.entity.Sheet;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.view.CourseTeacherView;
import com.passion.attendancesheet.room.view.SheetAttendanceView;
import com.passion.attendancesheet.room.view.SheetDetailView;

import java.util.List;

public class SheetRepository {

    SheetDao sheetDao;
    LiveData<List<Course>> allCourses;
    LiveData<List<SheetDetailView>> allSheets;

    public SheetRepository(Application application){
        SheetDatabase db = SheetDatabase.getDatabase( application );
        sheetDao  = db.sheetDao();
        allCourses = sheetDao.getAllCourses();
        allSheets = sheetDao.getAllSheets();
    }

    public void insertCourse(Course course ){
        new AsyncInsertCourse(sheetDao).execute(course);
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public void insertStudent( Student s ){
        new AsyncInsertStudent(sheetDao).execute(s);
    }

    public LiveData<List<Student>> getAllStudent(int course_id ){
        return sheetDao.getAllStudents(course_id);
    }

    public LiveData<List<CourseTeacherView>> getCourseTeachers( int course_id ){
        return sheetDao.getCourseTeacher(course_id);
    }


    public void insertSheet(Sheet s){
       new AsyncInsertSheet(sheetDao).execute(s);

    }

    public void deleteSheet( Sheet s ){
        new AsyncDeleteSheet(sheetDao).execute(s);
    }

    public void deleteSheedById( int sheed_id ){
        new AsyncDeleteSheetById(sheetDao).execute(sheed_id);
    }

    public void updateSheet( Sheet s ){
        new AsyncUpdateSheet(sheetDao).execute(s);
    }

    public LiveData<List<SheetDetailView>> getAllSheets(){
        return allSheets;
    }


    public void insertAttendance(Attendance attendance ){
        new AsyncInsertAttendance(sheetDao).execute(attendance);
    }

    public void deleteAttendance( Attendance attendance ){
       new AsyncDeleteAttendance( sheetDao ).execute(attendance);
    }

    public void deleteSheetAttendance( int sheet_id ){
        new AsyncDeleteSheetAttendance(sheetDao).execute(sheet_id);
    }

    public LiveData<List<SheetAttendanceView>> getSheetAttendance(int sheet_id ){
        return sheetDao.getSheetAttendance(sheet_id);
    }

    public LiveData<List<SheetDetailView>> getSheetDetailBySheetId( int sheet_id ){
        return sheetDao.getSheetDetailBySheetId(sheet_id);
    }

    private static class AsyncDeleteAttendance extends AsyncTask< Attendance, Void, Void > {

        SheetDao sheetDao;

        AsyncDeleteAttendance( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Attendance ... attendances ) {
            sheetDao.deleteAttendace(attendances[0]);
            return null;
        }
    }

    private static class AsyncInsertAttendance extends AsyncTask< Attendance, Void, Void > {

        SheetDao sheetDao;

        AsyncInsertAttendance( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Attendance ... attendances ) {
            sheetDao.insertAttendace( attendances[0] );
            return null;
        }
    }

    private static class AsyncDeleteSheetAttendance extends AsyncTask< Integer, Void, Void > {

        SheetDao sheetDao;

        AsyncDeleteSheetAttendance( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Integer ... sheets_id ) {
            sheetDao.deleteAttendanceOfSheet( sheets_id[0] );
            return null;
        }
    }

    private static class AsyncUpdateSheet extends AsyncTask< Sheet, Void, Void > {

        SheetDao sheetDao;

        AsyncUpdateSheet( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Sheet ... sheets) {
            sheetDao.updateSheet( sheets[0] );
            return null;
        }
    }

    private static class AsyncDeleteSheet extends AsyncTask< Sheet, Void, Void > {

        SheetDao sheetDao;

        AsyncDeleteSheet( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Sheet ... sheets) {
            sheetDao.deleteSheet( sheets[0] );
            return null;
        }
    }

    private static class AsyncDeleteSheetById extends AsyncTask< Integer, Void, Void > {

        SheetDao sheetDao;

        AsyncDeleteSheetById( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Integer ... sheets_id) {
            sheetDao.deleteSheetById( sheets_id[0] );
            return null;
        }
    }

    private static class AsyncInsertSheet extends AsyncTask< Sheet, Void, Void > {

        SheetDao sheetDao;

        AsyncInsertSheet( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Sheet ... sheets) {
            sheetDao.insertSheet( sheets[0] );
            return null;
        }
    }


    private static class AsyncInsertCourse extends AsyncTask< Course, Void, Void > {

        SheetDao sheetDao;

        AsyncInsertCourse( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            sheetDao.insertCourse( courses[0] );
            return null;
        }
    }

//    private static class AsyncGetAllCourse extends AsyncTask< Void, Void, List<Course> > {
//
//        SheetDao sheetDao;
//        List<Course> courses;
//        SheetListAdapter sheetListAdapter;
//
//        AsyncGetAllCourse(SheetDao sheetDao, SheetListAdapter sheetListAdapter){
//            this.sheetDao = sheetDao;
//            this.sheetListAdapter = sheetListAdapter;
//        }
//
//
//        @Override
//        protected List<Course> doInBackground(Void... voids) {
//            return sheetDao.getAllCourses();
//        }
//
//        @Override
//        protected void onPostExecute(List<Course> courses) {
//            super.onPostExecute(courses);
//
//            // TODO: load courses where it needed
//            sheetListAdapter.setCourseList( courses );
//
//        }
//
//        public List<Course> getCourses() {
//            return courses;
//        }
//    }

    /* insert student */
    private static class AsyncInsertStudent extends AsyncTask< Student, Void, Void > {

        SheetDao sheetDao;

        AsyncInsertStudent( SheetDao sheetDao ){
            this.sheetDao = sheetDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            sheetDao.insertStudent( students[0] );
            return null;
        }
    }

//    private static class AsyncLoadAllStudents extends AsyncTask< Void, Void, List<Student> > {
//
//        SheetDao sheetDao;
//        List<Student> students;
//
//        AsyncLoadAllStudents( SheetDao sheetDao ){
//            this.sheetDao = sheetDao;
//        }
//
//        @Override
//        protected List<Student> doInBackground(Void... voids) {
//            return sheetDao.getAllStudents();
//        }
//
//        @Override
//        protected void onPostExecute(List<Student> students) {
//            super.onPostExecute(students);
//
//            // TODO : load all students where it needed
//            this.students = students;
//        }
//    }

    public SheetDetailView getTopSheetsSync(){
        return sheetDao.getTopSheetsSync();
    }

}
