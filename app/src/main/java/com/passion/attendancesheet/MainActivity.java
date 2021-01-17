package com.passion.attendancesheet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.passion.attendancesheet.adapters.PageAdapter;
import com.passion.attendancesheet.room.SheetViewModel;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.entity.CourseTeacherCrossRef;
import com.passion.attendancesheet.room.entity.Student;
import com.passion.attendancesheet.room.entity.Teacher;
import com.passion.attendancesheet.utils.Accessory_tool;

import androidx.lifecycle.*;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 1;
    static int CUR_READ_TYPE = 0;

    ViewPager viewPager;
    PageAdapter pagerAdapter;
    SheetViewModel viewModel;
    FloatingActionButton fab;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferenceEditor;

    String curCourse;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SharedPreferences
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreference1), Context.MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreferences.edit();

        // ViewModel
        viewModel = ViewModelProviders.of(this).get(SheetViewModel.class);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            //  TODO : import sheet.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Ask for permission check
                if (checkPermission()) {
//                    importSheet();
                    new AsyncImport(this).execute();
                } else {
                    requestForPermission();
                }
            } else {
                // Go ahead
//                importSheet();
                new AsyncImport(this).execute();
            }

        });


        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), viewModel);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setTabTextColors(Color.rgb(80, 80, 80), Color.BLACK);
        tabLayout.setupWithViewPager(viewPager);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This Permission is needed to read file from your storage.")
                    .setPositiveButton("ok", (dialog, which) -> {
                        // Request Permission
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                        dialog.dismiss();
                    }).create().show();
        } else {
            // Request Permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Show popup and perform operation safely.
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                importSheet();
                new AsyncImport(this).execute();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void importSheet(Context context) {

        if (viewModel.sheetRepository.sheetDao.getTeachersCount() == 0) {
            CUR_READ_TYPE = 0;

            // Pop up to import courses and teachers
            context.getMainExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(context)
                            .setTitle("Import Basic Sheet ")
                            .setMessage("In order to use this attendance sheet app, you first need to import the basic sheet that consist courses and teachers information ")
                            .setPositiveButton("Ok", (dialog, which) -> {
                                // Import course and teachers sheet
                                Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                filePickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                filePickerIntent.setType("application/vnd.ms-excel");
                                ((MainActivity) context).startActivityForResult(filePickerIntent, READ_REQUEST_CODE);
                            })
                            .setNegativeButton("cancel", (dialog, which) -> {
                                // dismiss
                            }).create().show();
                }
            });

        } else {
            CUR_READ_TYPE = 1;
            // Import Student list
            Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            filePickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerIntent.setType("application/vnd.ms-excel");
            ((MainActivity) context).startActivityForResult(filePickerIntent, READ_REQUEST_CODE);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    assert uri != null;
                    File xl_file = new File(Environment.getExternalStorageDirectory() + "/" + Objects.requireNonNull(uri.getPath()).split(":")[1]);
                    FileInputStream myInput = new FileInputStream(xl_file);

                    // Create a POIFSFileSystem object
                    POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

                    // Create a workbook using the File System
                    HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

                    // Get the first sheet from workbook
                    HSSFSheet mySheet = myWorkBook.getSheetAt(0);

                    Iterator rowIter;
                    HSSFRow cur_row;
                    HSSFRow verifyRow;

                    Set<String> courses = new HashSet<String>();
                    List<String> teachers_with_courses = new ArrayList<String>();

                    // verify sheet ???? need to have strong verification
                    if (CUR_READ_TYPE == 0) {


                        // Read basic Sheet
                        rowIter = mySheet.rowIterator();
                        while (rowIter.hasNext()) {

                            // read
                            cur_row = (HSSFRow) rowIter.next();
                            if (cur_row != null) {
                                String heading = cur_row.getCell(0).toString();
                                if ( (cur_row.getPhysicalNumberOfCells() == 1) && heading.equalsIgnoreCase("courses") || heading.equalsIgnoreCase("course")) {

                                    while (rowIter.hasNext()) {
                                        cur_row = (HSSFRow) rowIter.next();
                                        if (cur_row != null && cur_row.getPhysicalNumberOfCells() == 1 && !cur_row.getCell(0).toString().equalsIgnoreCase("id")) {

                                            // read courses
                                            courses.add(cur_row.getCell(0).toString());
                                        } else if( !courses.isEmpty() && (cur_row.getPhysicalNumberOfCells() > 0 )&&cur_row != null && cur_row.getCell(0).toString().equalsIgnoreCase("id")){

                                            // save courses
                                            sharedPreferenceEditor.putStringSet(getString(R.string.courses), courses).commit();


                                                // read teachers with courses
                                                while (rowIter.hasNext()) {
                                                    cur_row = (HSSFRow) rowIter.next();
                                                    teachers_with_courses.add((int) cur_row.getCell(0).getNumericCellValue() + "/" + cur_row.getCell(1).toString() + "/" + cur_row.getCell(2));
                                                }



                                            Toast.makeText(this, "courses and teacher are readed successfully", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                } else {
                                    // invalid sheet
                                    invalidSheetPopup();
                                }
                            }
                        }

                        // show
                        StringBuilder temp = new StringBuilder();
                        for (String course : courses) {
                            temp.append(course).append(",");
                        }
                        Log.i("Debug Output : ", temp.toString());

                        for (String teacherCourse : teachers_with_courses) {
                            Log.i("Debug Output Teacher : ", teacherCourse);
                            // insert to database
                            teacherCourse = teacherCourse.trim();
                            String[] info = teacherCourse.split("/");
                            viewModel.insertTeacher(new Teacher(Integer.parseInt(info[0]), info[1]));

                            // insert all the courses mapping for this teacher
                            String[] coursesTought = info[2].split(",");
                            for (String courseTought : coursesTought) {
                                List<String> semestersWise = Accessory_tool.fetchCourseSemester(courseTought);
                                for (String courseSemster : semestersWise) {
                                    viewModel.insertCourseWithTeacherRef(new CourseTeacherCrossRef(courseSemster, Integer.parseInt(info[0])));
                                }
                            }

                        }


                    } else {

                        // read course student
                        String cur_courseReadied = "";
                        rowIter = mySheet.rowIterator();
                        List<String> student_list = new ArrayList<>();
                        boolean course_readied = false;

                        if (rowIter.hasNext()) {

                            cur_row = (HSSFRow) rowIter.next();
                            if (cur_row != null) {
                                if ((cur_row.getPhysicalNumberOfCells() == 2) && cur_row.getCell(0).toString().equalsIgnoreCase("course") && cur_row.getCell(1).toString().equalsIgnoreCase("semester")) {
                                    // read course name
                                    cur_row = (HSSFRow) rowIter.next();
                                    cur_courseReadied = cur_row.getCell(0).toString() + "-" + (int) cur_row.getCell(1).getNumericCellValue();
                                    course_readied = true;
                                } else {
                                    invalidSheetPopup();
                                }
                            }
                        } else {
                            invalidSheetPopup();
                        }

                        // if valid
                        if (course_readied) {

                            while (rowIter.hasNext()) {
                                cur_row = (HSSFRow) rowIter.next();
                                if (cur_row != null && !cur_row.getCell(0).toString().isEmpty()) {
                                    if (cur_row.getPhysicalNumberOfCells() == 2 && cur_row.getCell(0).toString().equalsIgnoreCase("roll") && cur_row.getCell(1).toString().equalsIgnoreCase("name")) {

                                        while (rowIter.hasNext()) {
                                            cur_row = (HSSFRow) rowIter.next();
                                            student_list.add((int) cur_row.getCell(0).getNumericCellValue() + "/" + cur_row.getCell(1).toString());
                                        }
                                    }
                                    else{
                                        invalidSheetPopup();
                                    }
                                }
                            }

                            // courses readied successfully
                            for (String student : student_list) {
                                String[] student_data = student.split("/");
                                Log.i("Debug student list : ", student);
                                viewModel.insertStudents(new Student(cur_courseReadied, Integer.parseInt(student_data[0]), student_data[1]));
                            }

                            curCourse = cur_courseReadied;
                            viewModel.getAllStudents(cur_courseReadied).observe(this, students -> {
                                if (students.isEmpty()) {
                                    invalidSheetPopup();
                                } else {
                                    // insert the student list as course added
                                    viewModel.insertCourses(new Course(curCourse));
                                }
                            });
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void invalidSheetPopup() {
        // pop up alert , Corrupt xl file
        new AlertDialog.Builder(this)
                .setTitle("Invalid Sheet")
                .setMessage("You have selected different sheet please select the basic sheet consist of teacher and course information")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                }).create().show();
    }

    class AsyncImport extends AsyncTask<Void, Void, Void> {

        Context context;

        AsyncImport(Context context) {
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected Void doInBackground(Void... voids) {
            importSheet(context);
            return null;
        }
    }
}
