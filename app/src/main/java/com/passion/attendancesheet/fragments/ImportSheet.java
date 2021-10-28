package com.passion.attendancesheet.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentImportSheetBinding;
import com.passion.attendancesheet.model.AttendanceSheetDao;
import com.passion.attendancesheet.model.AttendanceSheetDatabase;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;
import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.TeacherCourseCross;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.passion.attendancesheet.model.entity.Subject;
import com.passion.attendancesheet.utils.Accessory_tool;

import timber.log.Timber;


public  class ImportSheet extends Fragment {

    private static final int READ_WRITE_REQUEST_CODE = 101;
    FragmentImportSheetBinding binding;
    private AttendanceSheetViewModel viewModel;
    NavController navController;
    String CR_courseId = null;

    public ImportSheet() {
        // Required empty public constructor

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get( AttendanceSheetViewModel.class );
        navController = NavHostFragment.findNavController(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentImportSheetBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the current course for a respective CR from firebase
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        currentUser.reload();
        db.getReference().child("crs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot courses : snapshot.getChildren() ){
                    for( DataSnapshot crs : courses.getChildren() ){
                        if(crs.child("email").getValue(String.class).equals(currentUser.getEmail())){
                            CR_courseId = courses.getKey();
                            CR_courseId = CR_courseId.split(" ")[0] + "-" + Accessory_tool.getIntFromRoman(CR_courseId.split(" ")[1]);
                            Timber.d("Course ID readed : " + CR_courseId);
                            break;
                        }
                    }

                    if( CR_courseId != null ){
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Import sheet if successful navigate to CR home in async task postExecution
        binding.importSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Import the sheet from storage

                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up_animation));

                // Check Permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Ask for permission check
                    if (checkReadPermission() && checkWritePermission() ) {
                        new ImportSheet.AsyncImport(getContext()).execute();
                    } else {
                        requestForPermission();
                    }
                } else {
                    new ImportSheet.AsyncImport(getContext()).execute();
                }
            }

        });
    }




    private boolean checkReadPermission() {
        int result1 = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkWritePermission(){
        int result2 = ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE );
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Needed")
                    .setMessage("This Permission is needed to read file from your storage.")
                    .setPositiveButton("ok", (dialog, which) -> {
                        // Request Permission
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_REQUEST_CODE);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                        dialog.dismiss();
                    }).create().show();
        } else {
            // Request Permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        Timber.i("onActivityResult Called");
        if (requestCode == READ_WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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
                    List<String> teachers = new ArrayList<String>();
                    String subjects = null;


                    // read course student
                    String cur_courseReadied = "";
                    rowIter = mySheet.rowIterator();
                    List<String> student_list = new ArrayList<>();
                    boolean course_readied = false;

                    if (rowIter.hasNext()) {

                        cur_row = (HSSFRow) rowIter.next();
                        if (cur_row != null) {
                            if ((cur_row.getPhysicalNumberOfCells() == 2) && cur_row.getCell(0).toString().equalsIgnoreCase("course") ) {
                                // read course name
                                cur_courseReadied = cur_row.getCell(1).toString();
                                Timber.i("Course Readed" + cur_courseReadied);

                                if( cur_courseReadied.equals(CR_courseId ) == false ){
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Invalid Sheet")
                                            .setMessage("Please Import Your Course Sheet")
                                            .setPositiveButton("OK", ( dialog, which ) -> {
                                                dialog.dismiss();
                                            })
                                            .create().show();
                                            return;
                                }
                                // Read Teachers
                                cur_row = (HSSFRow) rowIter.next();
                                if( cur_row.getPhysicalNumberOfCells() == 2 && cur_row.getCell(0).toString().equalsIgnoreCase("Teachers")){
                                    teachers = Arrays.asList(cur_row.getCell(1).toString().split("\\|"));
                                    Timber.i(cur_row.getCell(1).toString());
                                }

                                // Read Subjects
                                cur_row = (HSSFRow) rowIter.next();
                                if( cur_row.getPhysicalNumberOfCells() == 2 && cur_row.getCell(0).toString().equalsIgnoreCase("Subjects")){
                                    subjects =cur_row.getCell(1).toString();
                                    Timber.i( cur_row.getCell(1).toString() );
                                }

                                course_readied = true;

                            } else {
                                invalidSheetPopup();
                                return;
                            }
                        }
                    } else {
                        invalidSheetPopup();
                        return;
                    }

                    Timber.i("Course Read %s", course_readied);


                    // if valid
                    if (course_readied) {

                        while ( rowIter.hasNext() == true ) {
                            Timber.d("Inside loop");
                            cur_row = (HSSFRow) rowIter.next();
                            if ( cur_row.getPhysicalNumberOfCells() != 0 ) {
                                if (cur_row.getPhysicalNumberOfCells() == 2 && cur_row.getCell(0).toString().equalsIgnoreCase("roll") && cur_row.getCell(1).toString().equalsIgnoreCase("name")) {
                                    Timber.i(cur_row.toString());
                                    while (rowIter.hasNext()) {
                                        cur_row = (HSSFRow) rowIter.next();
                                        student_list.add((int) cur_row.getCell(0).getNumericCellValue() + "/" + cur_row.getCell(1).toString());
                                    }
                                } else {
                                    invalidSheetPopup();
                                    return;
                                }
                            }
                        }

                        Timber.i("Sheet readied Successfully");

                        // Add Courses
//                        if (student_list.isEmpty()) {
//                            invalidSheetPopup();
//                        } else {
//                            // course added
//
//                        }


                        // Add students
                        new AsyncInsertStudentAndTeachersData(viewModel, student_list, teachers, subjects).execute( cur_courseReadied );



                    }

                } catch (Exception e) {
                    Timber.d( " Exception " + e.getMessage() );
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_WRITE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Show popup and perform operation safely.
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                new ImportSheet.AsyncImport(getContext()).execute();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void invalidSheetPopup() {
        //Message for alerts
        String msg1 = "You have select different or invalid sheet please select the sheet with Course Students list.";
        String msg0 = "You have selected different sheet please select the basic sheet consist of teacher and course information";

        String msg = msg1;

        new AlertDialog.Builder(getContext())
                .setTitle("Invalid Sheet")
                .setMessage(msg)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void importSheet(Context context) {

        // Import Student list
        Intent filePickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        filePickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerIntent.setType("application/vnd.ms-excel");
        startActivityForResult(filePickerIntent, READ_WRITE_REQUEST_CODE);

    }


    // AsyncImport to import the sheet in background
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


    // Insert all the students and teachers data along with TeacherCourseCross
    class AsyncInsertStudentAndTeachersData extends AsyncTask<String, Void, String>{

        AttendanceSheetViewModel viewModel;
        List<String> student_list;
        List<String> teachers;
        String subjects;

        AsyncInsertStudentAndTeachersData(AttendanceSheetViewModel viewModel, List<String> student_list, List<String> teachers, String subjects ){
            this.viewModel = viewModel;
            this.student_list = student_list;
            this.teachers = teachers;
            this.subjects = subjects;
        }


        @Override
        protected String doInBackground(String... course_name ) {
            viewModel.addCourse(new Course(course_name[0], student_list.size()));
            return course_name[0];
        }

        @Override
        protected void onPostExecute(String course_name ) {

            for( String student : student_list ){
                String[] student_data = student.split("\\/", 2 );
                viewModel.addStudent( new Student( Integer.parseInt(student_data[0]), student_data[1], course_name )) ;
            }

            for( int i = 0;i < teachers.size(); i++ ){
                viewModel.addTeacher( new Teacher( i,  teachers.get(i).trim() ) );
                viewModel.addCourseTeacher( new TeacherCourseCross( i, course_name ));

            }

            viewModel.addSubject( new Subject( course_name, subjects ));

            // redirect to previous HOME cr fragment
             navController.popBackStack();
        }


    }
}