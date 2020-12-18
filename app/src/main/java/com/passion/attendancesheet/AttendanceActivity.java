package com.passion.attendancesheet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.passion.attendancesheet.adapters.SheetListAdapter;
import com.passion.attendancesheet.adapters.StudentListAdapter;
import com.passion.attendancesheet.room.SheetViewModel;
import com.passion.attendancesheet.room.entity.Attendance;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.entity.Sheet;
import com.passion.attendancesheet.room.entity.SheetWithAttendace;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.entity.Teacher;
import com.passion.attendancesheet.room.view.CourseTeacherView;
import com.passion.attendancesheet.room.view.SheetAttendanceView;
import com.passion.attendancesheet.room.view.SheetDetailView;
import com.passion.attendancesheet.utils.Accessory_tool;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    public static String COURSE = "course", LECTURE = "lecture", TEACHER = "teacher", SUBJECT = "subject", MODE="mode", HISTORY_DETAILS = "history_details";
    RecyclerView student_list;
    StudentListAdapter student_list_adapter;
    SheetViewModel viewModel;
    TextView empty_student;
    CardView header;
    Intent sheet_intent;

    int curCourseId;
    int sheet_id;
    String curCourseName;
    int curTeacherId;
    String curTeacherName;
    int curLecture;
    int curSem;
    String curSubject;
    String mode;

    SheetDetailView sheetDetails;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // configure action bar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        // header && intent
        sheet_intent = getIntent();
        mode = sheet_intent.getExtras().get(MODE).toString();
        header = findViewById(R.id.sheet_header);

        // get header components
        TextView course = header.findViewById(R.id.course_name);
        TextView lecture = header.findViewById(R.id.lecture);
        TextView teacher = header.findViewById(R.id.teacher);
        TextView subject = header.findViewById(R.id.subject);

        // get data of header
        if( mode.equals(getString(R.string.normal))){
            curCourseName = ((Course)sheet_intent.getSerializableExtra(COURSE)).name;
            curCourseName += " " + ((Course)sheet_intent.getSerializableExtra(COURSE)).semester;

            curCourseId = ((Course)sheet_intent.getSerializableExtra(COURSE)).course_id;
            curTeacherName = ((CourseTeacherView)sheet_intent.getSerializableExtra(TEACHER)).teacher_name;
            curTeacherId = ((CourseTeacherView)sheet_intent.getSerializableExtra(TEACHER)).teacher_id;
            curLecture  = Accessory_tool.getIntFromRoman(sheet_intent.getExtras().get(LECTURE).toString());
            curSubject = sheet_intent.getExtras().get(SUBJECT).toString();
            curSem = ((Course)sheet_intent.getSerializableExtra(COURSE)).semester;

        }
        else{
            // load edit tab for edit and udpate
           sheetDetails = (SheetDetailView)sheet_intent.getExtras().get(HISTORY_DETAILS);
           curCourseId = sheetDetails.course_id;
           curCourseName = sheetDetails.course_name;
           curTeacherId = sheetDetails.teacher_id;
           curTeacherName = sheetDetails.teacher_name;
           curLecture = sheetDetails.lecture;
           curSubject = sheetDetails.subject_name;
           curSem = sheetDetails.semester;
        }

        // fill data
        course.setText( curCourseName + " " + Accessory_tool.getRomanFromInt(curSem) );
        lecture.setText( "Lecture " + curLecture);
        teacher.setText( "By " + curTeacherName );
        subject.setText( curSubject );

        // view Model
        viewModel = ViewModelProviders.of(this).get( SheetViewModel.class );


        // student list
        student_list = findViewById(R.id.student_list);
        student_list_adapter = new StudentListAdapter(this, mode );
        student_list.setAdapter(student_list_adapter);
        student_list.setLayoutManager( new LinearLayoutManager(this));
        empty_student = findViewById(R.id.empty_no_student);

        if( mode.equals(getString(R.string.normal))){
            // Show all the students for the curCourseId
            viewModel.getAllStudents(curCourseId).observe(this, new Observer<List<Student>>() {
                @Override
                public void onChanged(List<Student> students) {
                    student_list_adapter.setStudents( students );

                    if( students.size() == 0 ){
                        empty_student.setVisibility(View.VISIBLE);
                    }
                    else{
                        empty_student.setVisibility(View.GONE);
                    }

                }
            });
        }
        else{
            // Shows the existing attendance sheet
            viewModel.getSheetAttendance( sheetDetails.sheet_id ).observe(this, new Observer<List<SheetAttendanceView>>() {
                @Override
                public void onChanged(List<SheetAttendanceView> sheetAttendanceViews) {
                    student_list_adapter.setStudentPresent( sheetAttendanceViews );
                    if( sheetAttendanceViews.size() == 0 ){
                        empty_student.setVisibility(View.VISIBLE);
                    }
                    else{
                        empty_student.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.attendance_sheet_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if( mode.equals(getString(R.string.normal)) || mode.equals(getString(R.string.editToNormal))){
            menu.getItem(0).setVisible(false);
            actionBar.setBackgroundDrawable( getResources().getDrawable(R.color.colorPrimaryDark));
        }
        else if( mode.equals(getString(R.string.edit))){
            menu.getItem(1).setVisible(false);
            actionBar.setBackgroundDrawable( getResources().getDrawable(R.color.colorPrimary) );
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.edit:
                mode = getString(R.string.editToNormal);
                StudentListAdapter.setMode(mode);
                invalidateOptionsMenu();
                List<SheetAttendanceView> sheetAttendanceViews = student_list_adapter.getStudentPresents();
                empty_student.setVisibility(View.GONE);
                viewModel.getAllStudents( curCourseId ).observe(this, new Observer<List<Student>>() {
                    @Override
                    public void onChanged(List<Student> students) {
                             student_list_adapter.showExistingSheetWithEdit( students, sheetAttendanceViews );
                    }
                });
                break;

            case R.id.save :
                // fetch data from the recycler view
                if(student_list.getChildCount() == 0 ){
                    // TODO : popup a snakebar message (Optional) instead think for verifing insertion of course that it must have student
                }
                else{
                    // save sheet details
                    String date_time = new SimpleDateFormat("MMM dd, yyyy-hh:mm a").format(Calendar.getInstance().getTime());
                    Sheet newSheet = new Sheet( date_time, curCourseId, curLecture, curSubject, curTeacherId);

                    if( mode.equals(getString(R.string.editToNormal))){
                        // delete the selected sheet and thier attendance , to feed new sheet on that places
                        int selected_sheet = Integer.parseInt(sheet_intent.getExtras().get(COURSE).toString());
                        viewModel.deleteSheetById(selected_sheet);
                        viewModel.deleteSheetAttendance(selected_sheet);
                    }

                    viewModel.insertSheet( newSheet );


                    viewModel.getAllSheets().observe(this, new Observer<List<SheetDetailView>>() {
                        @Override
                        public void onChanged(List<SheetDetailView> sheetDetailViews) {
                            if (!sheetDetailViews.isEmpty()) {
                                int newSheet_id = sheetDetailViews.get(0).sheet_id;
                                int student_count = student_list_adapter.getItemCount();

                                List<Student> studentList = student_list_adapter.getStudents();
                                for (int i = 0; i < student_count; i++) {

                                    if (StudentListAdapter.studentPresentIndex.contains(i)) {
                                        Student s = studentList.get(i);
                                        viewModel.insertAttendance(new Attendance(newSheet_id, s.student_id));
                                    }
                                }
                            }
                        }
                    });


                }

                finish();
                break;
            case R.id.send:
                // TODO: save the sheet to database and open the implicit intent for sending
                // TODO: create xl file with attendance
               if( prepareExcelFile() ){
                   System.out.println("writen");
               }
               else{
                   System.out.println("not writen");
               }

            break;

            case android.R.id.home:
                onBackPressed();
        }

        return true;
    }

    public static boolean isExternalStorageAvaiable(){
        String extStorageState = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public static boolean isExternalStorageReadOnly(){
        String extStorageState = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    public boolean prepareExcelFile(){

        if( !isExternalStorageAvaiable() || isExternalStorageReadOnly() ){
            return false;
        }
        else{

            boolean success = false;

            Workbook wb = new HSSFWorkbook();
            Cell c = null;

            CellStyle cs = wb.createCellStyle();

            cs.setFillBackgroundColor(HSSFColor.BLACK.index);

            org.apache.poi.ss.usermodel.Sheet sheet1 = wb.createSheet();
            Row row = sheet1.createRow(0);
            c = row.createCell(0);
            c.setCellValue("Course");
            c.setCellStyle(cs);

            c = row.createCell(1);
            c.setCellValue( curCourseName + " " + Accessory_tool.getRomanFromInt(curSem)  );
            c.setCellStyle(cs);

            if( mode.equals("normal")){
                int stu_count = student_list_adapter.getItemCount();
                List<Student> studentList = student_list_adapter.getStudents();
                for( int i = 0, j = 1; i < stu_count; i++ ){

                    if( StudentListAdapter.studentPresentIndex.contains(i)){
                        // add to sheet
                        row = sheet1.createRow( row.getRowNum() + 1);
                        row.createCell(0).setCellValue(j++);
                        row.createCell(1).setCellValue(studentList.get(i).student_id);
                        row.createCell(2).setCellValue(studentList.get(i).name);

                    }
                }
            }
            else{
                List<SheetAttendanceView> stu_presents = student_list_adapter.getStudentPresents();
                for( int i = 0; i < stu_presents.size(); i++ ){
                    row = sheet1.createRow( row.getRowNum() + 1);
                    row.createCell(0).setCellValue(i);
                    row.createCell(1).setCellValue( stu_presents.get(i).student_id);
                    row.createCell(2).setCellValue( stu_presents.get(i).student_name);
                }
            }


            File file = new File( getExternalFilesDir(null),  curCourseName+ curSem + curLecture + ".xls");
            FileOutputStream os = null;

            try{
                os = new FileOutputStream(file);
                wb.write(os);
                Log.w("FileUtils", "Writing file " + file );
                success = true;
            }
            catch ( IOException e){
                Log.w("fileUtiles", "Error writing" + file , e);
            }
            finally {
                try{
                    if( os != null ){
                        os.close();
                    }
                }catch ( Exception ex){

                }
            }

            if( success ){
                Intent intent = new Intent( Intent.ACTION_SEND );
                try {
                    if (file.exists()) {
                        intent.setType("text/plain");
//                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.toURI()));
//                        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    intent.setData( Uri.fromFile(file));
                        startActivity(Intent.createChooser(intent, "Share"));
                    }
                }
                catch ( Exception e ){

                }
            }
            return success;
        }

    }

}
