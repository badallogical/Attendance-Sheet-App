package com.passion.attendancesheet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.passion.attendancesheet.adapters.StudentListAdapter;
import com.passion.attendancesheet.room.SheetViewModel;
import com.passion.attendancesheet.room.entity.Attendance;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.entity.Sheet;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.view.CourseTeacherView;
import com.passion.attendancesheet.room.view.SheetAttendanceView;
import com.passion.attendancesheet.room.view.SheetDetailView;
import com.passion.attendancesheet.utils.Accessory_tool;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AttendanceActivity extends AppCompatActivity {

    static final String LOG = AttendanceActivity.class.getName();
    static final String STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    static final int PERMISSION_REQUEST_CODE = 1;

    public static String COURSE = "course", LECTURE = "lecture", TEACHER = "teacher", SUBJECT = "subject", MODE = "mode", HISTORY_DETAILS = "history_details";
    RecyclerView student_list;
    StudentListAdapter student_list_adapter;
    SheetViewModel viewModel;
    TextView empty_student;
    CardView header;
    Intent sheet_intent;

    String curCourseId;
    String curCourseName;
    int curTeacherId;
    String curTeacherName;
    int curLecture;
    String curSem;
    String curSubject;
    static String dateTime;
    String mode;

    SheetDetailView sheetDetails;
    ActionBar actionBar;

    static File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // configure action bar
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);


        // header && intent
        sheet_intent = getIntent();
        mode = Objects.requireNonNull(Objects.requireNonNull(sheet_intent.getExtras()).get(MODE)).toString();
        header = findViewById(R.id.sheet_header);

        // get header components
        TextView course = header.findViewById(R.id.course_name);
        TextView lecture = header.findViewById(R.id.lecture);
        TextView teacher = header.findViewById(R.id.teacher);
        TextView subject = header.findViewById(R.id.subject);

        // get data of header
        try {
            if (mode.equals(getString(R.string.normal))) {
                curCourseId = ((Course) Objects.requireNonNull(sheet_intent.getSerializableExtra(COURSE))).course_id;
                curCourseName = curCourseId.split("-")[0];
                curTeacherName = ((CourseTeacherView) Objects.requireNonNull(sheet_intent.getSerializableExtra(TEACHER))).teacher_name;
                curTeacherId = ((CourseTeacherView) Objects.requireNonNull(sheet_intent.getSerializableExtra(TEACHER))).teacher_id;
                curLecture = Accessory_tool.getIntFromRoman(Objects.requireNonNull(sheet_intent.getExtras().get(LECTURE)).toString());
                curSubject = Accessory_tool.convertToCamelCase(Objects.requireNonNull(sheet_intent.getExtras().get(SUBJECT)).toString());
                curSem = curCourseId.split("-")[1];

            } else {
                // load edit tab for edit and udpate
                sheetDetails = (SheetDetailView) sheet_intent.getExtras().get(HISTORY_DETAILS);
                assert sheetDetails != null;
                curCourseId = sheetDetails.course_id;
                curCourseName = sheetDetails.course_id.split("-")[0];
                curTeacherId = sheetDetails.teacher_id;
                curTeacherName = sheetDetails.teacher_name;
                curLecture = sheetDetails.lecture;
                curSubject = Accessory_tool.convertToCamelCase(sheetDetails.subject_name);
                curSem = sheetDetails.course_id.split("-")[1];
            }
        } catch (NullPointerException e) {
            Log.d(LOG, Objects.requireNonNull(e.getMessage()));
        }

        // fill data
        course.setText(curCourseName + " " + Accessory_tool.getRomanFromInt(Integer.parseInt(curSem)));
        lecture.setText("Lecture " + curLecture);
        teacher.setText("By " + curTeacherName);
        subject.setText(curSubject);

        // view Model
        viewModel = ViewModelProviders.of(this).get(SheetViewModel.class);


        // student list
        student_list = findViewById(R.id.student_list);
        student_list_adapter = new StudentListAdapter(this, mode);
        student_list.setAdapter(student_list_adapter);
        student_list.setLayoutManager(new LinearLayoutManager(this));
        empty_student = findViewById(R.id.empty_no_student);

        if (mode.equals(getString(R.string.normal))) {
            // Show all the students for the curCourseId
            viewModel.getAllStudents(curCourseId).observe(this, students -> {
                student_list_adapter.setStudents(students);

                if (students.size() == 0) {
                    empty_student.setVisibility(View.VISIBLE);
                } else {
                    empty_student.setVisibility(View.GONE);
                }

            });
        } else {
            // Shows the existing attendance sheet
            viewModel.getSheetAttendance(sheetDetails.sheet_id).observe(this, sheetAttendanceViews -> {
                student_list_adapter.setStudentPresent(sheetAttendanceViews);
                if (sheetAttendanceViews.size() == 0) {
                    empty_student.setVisibility(View.VISIBLE);
                } else {
                    empty_student.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.attendance_sheet_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mode.equals(getString(R.string.normal)) || mode.equals(getString(R.string.editToNormal))) {
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimaryDark));
        } else if (mode.equals(getString(R.string.edit))) {
            menu.getItem(2).setVisible(true);
            menu.getItem(1).setVisible(false);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.edit:
                mode = getString(R.string.editToNormal);
                StudentListAdapter.setMode(mode);
                invalidateOptionsMenu();
                List<SheetAttendanceView> sheetAttendanceViews = student_list_adapter.getStudentPresents();
                empty_student.setVisibility(View.GONE);
                viewModel.getAllStudents(curCourseId).observe(this, students -> student_list_adapter.showExistingSheetWithEdit(students, sheetAttendanceViews));
                break;

            case R.id.save:
                saveSheet();
                finish();
                break;
            case R.id.send:
                prepareExcelFileAndSend();
                break;

            case android.R.id.home:
                onBackPressed();
        }

        return true;
    }

    public static boolean isExternalStorageAvaiable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    public boolean prepareExcelFileAndSend() {

        if (!isExternalStorageAvaiable() || isExternalStorageReadOnly()) {
            return false;
        } else {

            // Create Sheet with current attendance data
            boolean success = false;
            Workbook wb = new HSSFWorkbook();
            Cell c;

            CellStyle cs = wb.createCellStyle();

            cs.setFillBackgroundColor(HSSFColor.BLACK.index);

            org.apache.poi.ss.usermodel.Sheet sheet1 = wb.createSheet();

            // cell style
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment( CellStyle.ALIGN_CENTER );

            Row row = sheet1.createRow(0);
            c = row.createCell(0);
            c.setCellValue("Course");
            c.setCellStyle(cellStyle);

            c = row.createCell(1);
            c.setCellValue(curCourseName);
            c.setCellStyle(cs);
            c.setCellStyle(cellStyle);

            row = sheet1.createRow(1);
            c = row.createCell(0);
            c.setCellValue("Sr. No.");
            c.setCellStyle(cellStyle);

            c = row.createCell(1);
            c.setCellValue("Roll No.");
            c.setCellStyle(cellStyle);

            c = row.createCell(2);
            c.setCellValue("Students Present");
            cellStyle.setWrapText(true);
            c.setCellStyle(cellStyle);

//
//            if (mode.equals("normal")) {
//                int stu_count = student_list_adapter.getItemCount();
//                List<Student> studentList = student_list_adapter.getStudents();
//                for (int i = 0; i < stu_count; i++) {
//
//                    if (StudentListAdapter.studentPresentIndex.contains(i)) {
//                        // add to sheet
//                        row = sheet1.createRow(row.getRowNum() + 1);
//                        c = row.createCell(0);
//                        c.setCellValue(i+1);
//                        c.setCellStyle( cellStyle );
//
//                        c = row.createCell(1);
//                        c.setCellValue(studentList.get(i).student_no);
//                        c.setCellStyle( cellStyle );
//
//                        c = row.createCell(2);
//                        c.setCellValue(studentList.get(i).name);
//                        c.setCellStyle( cellStyle );
//                    }
//                }
//            } else {
                List<SheetAttendanceView> stu_presents = student_list_adapter.getStudentPresents();
                for (int i = 0; i < stu_presents.size(); i++) {
                    // add to sheet
                    row = sheet1.createRow(row.getRowNum() + 1);
                    c = row.createCell(0);
                    c.setCellValue(i+1);
                    c.setCellStyle( cellStyle );

                    c = row.createCell(1);
                    c.setCellValue(stu_presents.get(i).student_no);
                    c.setCellStyle( cellStyle );

                    c = row.createCell(2);
                    c.setCellValue(Accessory_tool.convertToCamelCase(stu_presents.get(i).student_name));
                    cellStyle.setWrapText(true);
                }
//            }

            dateTime = new SimpleDateFormat("MMM dd, yyyy-hh:mm a", Locale.US).format(Calendar.getInstance().getTime());
            file = new File(getExternalFilesDir(null), curCourseName + curSem + " " + curLecture + " " + dateTime.split(",")[0] + ".xls");

            try (FileOutputStream os = new FileOutputStream(file)) {
                wb.write(os);
                Log.w("FileUtils", "Writing file " + file);
                success = true;
            } catch (IOException e) {
                Log.w("fileUtiles", "Error writing" + file, e);
            }

            if (success) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission() ) {
                        sendFile(file);
                    } else {
                        requestForPermission();
                    }
                } else {
                    sendFile(file);
                }

            } else {
                // TODO: pop up Notification for file and storage issue
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("File Storage issue, can't save file")
                        .setPositiveButton( "Ok", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Simple close
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            return success;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AttendanceActivity.this, AttendanceActivity.STORAGE_PERMISSION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermission() {
        if( ActivityCompat.shouldShowRequestPermissionRationale(this,STORAGE_PERMISSION )){

            // Alert Dialog
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed for sharing file to other apps")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(AttendanceActivity.this, new String[] {STORAGE_PERMISSION}, PERMISSION_REQUEST_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss()).create().show();
        }
        else{
            ActivityCompat.requestPermissions(AttendanceActivity.this, new String[] { AttendanceActivity.STORAGE_PERMISSION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(AttendanceActivity.this, "Permission Successfull", Toast.LENGTH_SHORT).show();
                sendFile(file);
            } else {
                Toast.makeText(AttendanceActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void saveSheet() {
        // fetch data from the recycler view
        if (student_list.getChildCount() == 0) {
            // TODO : popup a snakebar message (Optional) instead think for verifying insertion of course that it must have student

        } else {
            // save sheet details
            dateTime = new SimpleDateFormat("MMM dd, yyyy-hh:mm a", Locale.US).format(Calendar.getInstance().getTime());
            Sheet newSheet = new Sheet(dateTime, curCourseId, curLecture, curSubject, curTeacherId);

            if (mode.equals(getString(R.string.editToNormal))) {
                // delete the selected sheet and thier attendance , to feed new sheet on that places
                int selected_sheet = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(sheet_intent.getExtras()).get(COURSE)).toString());
                viewModel.deleteSheetById(selected_sheet);
                viewModel.deleteSheetAttendance(selected_sheet);
            }

            viewModel.insertSheet(newSheet);


            viewModel.getAllSheets().observe(this, sheetDetailViews -> {
                if (!sheetDetailViews.isEmpty()) {
                    int newSheet_id = sheetDetailViews.get(0).sheet_id;
                    int student_count = student_list_adapter.getItemCount();

                    List<Student> studentList = student_list_adapter.getStudents();
                    for ( Student s : studentList ) {

                        if (StudentListAdapter.studentPresentIndex.contains( s.student_no )) {
                            viewModel.insertAttendance(new Attendance( newSheet_id, s.course_id, s.student_no));
                        }
                    }
                }
            });


        }
    }

    private boolean sendFile(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, file.getName());
        try {
            if (file.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri path = FileProvider.getUriForFile(AttendanceActivity.this, "com.passion.attendancesheet", file);
                    intent.putExtra(Intent.EXTRA_STREAM, path);
                } else {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("plain/*");
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.d(LOG, Objects.requireNonNull(e.getMessage()));
        }
        return true;
    }

}
