package com.passion.attendancesheet;

import android.Manifest;
import androidx.appcompat.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import static android.content.DialogInterface.*;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 1;
    static int CUR_READ_TYPE = 0;
    private int ACTION_BAR_MODE = 0;
    private String SEL_COURSE_ID = "";
    private static boolean IMPORTING_SHEET = false;

    ActionBar actionBar;

    View selectedCourseView;

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

        // Action bar
        actionBar = getSupportActionBar();

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
                    new AsyncImport(this).execute();
                } else {
                    requestForPermission();
                }
            } else {
                new AsyncImport(this).execute();
            }

        });


        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), viewModel, this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setTabTextColors(Color.rgb(80, 80, 80), Color.BLACK);
        tabLayout.setupWithViewPager(viewPager);
    }


    /* Action bar menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if( ACTION_BAR_MODE == 1 ){
            // Sheet Delete Action Bar
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            actionBar.setBackgroundDrawable(getDrawable(R.color.colorPrimaryDark));

        }
        else{
            // Setting Option
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            actionBar.setBackgroundDrawable( getDrawable(R.color.colorPrimary));

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() ){
            case R.id.delete:

                if( SEL_COURSE_ID.isEmpty() ){

                    // if course_id is empty
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Invalid Course ");
                    builder.setMessage("Course Id is empty can't delete the sheet");
                    builder.setPositiveButton("OK", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                }else{

                    // Confirmation Delete dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Confirmation");
                    builder.setIcon(R.drawable.ic_round_warning_24);
                    builder.setMessage("Deleting the list will delete all the course date include students, and history of attendance.");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.deleteStudentsByCourseId(SEL_COURSE_ID);
                            Toast.makeText(MainActivity.this, "Course deleted Successfully", Toast.LENGTH_LONG).show();

                            // Reset the actionbar to setting
                            resetActionBar();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            // Reset the actionbar to setting
                            resetActionBar();

                        }
                    });

                    builder.create().show();




                }

            break;

            case R.id.setting:
                break;
        }
        return true;
    }

    private void resetActionBar(){
        // Reset Action Bar Mode
        ACTION_BAR_MODE = 0;
        invalidateOptionsMenu();
        pagerAdapter.getSheetList().getSheetListAdapter().notifyDataSetChanged();
        selectedCourseView.setBackgroundResource(R.color.normalCardColor);

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
                        if (course_readied ) {

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

                                if(IMPORTING_SHEET){
                                    if (students.isEmpty()) {
                                        invalidSheetPopup();
                                    } else {
                                        // insert the student list as course added
                                        viewModel.insertCourses(new Course(curCourse));
                                    }

                                    IMPORTING_SHEET = false;
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
        //Message for alerts
        String msg1 = "You have select different or invalid sheet please select the sheet with Course Students list.";
        String msg0 = "You have selected different sheet please select the basic sheet consist of teacher and course information";

        String msg = "";
        if( CUR_READ_TYPE == 0 ){
            // Invalid basic sheet
            msg = msg0;
        }
        else{
            // Invalid Information Sheet
            msg = msg1;
        }

        new AlertDialog.Builder(this)
                .setTitle("Invalid Sheet")
                .setMessage(msg)
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
            IMPORTING_SHEET = true;
            importSheet(context);
            return null;
        }
    }

    public void setACTION_BAR_MODE(int ACTION_BAR_MODE) {
        this.ACTION_BAR_MODE = ACTION_BAR_MODE;
    }

    public int getACTION_BAR_MODE() {
        return ACTION_BAR_MODE;
    }

    public void setSelectedCourseId( String courseId ){
        this.SEL_COURSE_ID = courseId;
    }

    public void setSelectedCourseView( View v){
        this.selectedCourseView = v;
    }
}
