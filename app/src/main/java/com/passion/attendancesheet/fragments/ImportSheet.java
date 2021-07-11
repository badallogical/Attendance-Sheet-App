package com.passion.attendancesheet.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.passion.attendancesheet.databinding.FragmentImportSheetBinding;
import com.passion.attendancesheet.model.AttendanceSheetDao;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;
import com.passion.attendancesheet.model.entity.Course;
import com.passion.attendancesheet.model.entity.Student;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import timber.log.Timber;


public class ImportSheet extends Fragment {

    private static final int READ_REQUEST_CODE = 101;
    FragmentImportSheetBinding binding;
    private AttendanceSheetViewModel viewModel;

    public ImportSheet() {
        // Required empty public constructor

    }

    @Override
    public void onStart() {
        super.onStart();

        // if sheet already imported, redirect to admin home
        if (checkIfSheetAvailable() == true) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(ImportSheetDirections.actionImportSheetToHome());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get( AttendanceSheetViewModel.class );

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


        binding.importSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Import the sheet from storage
                // Check Permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Ask for permission check
                    if (checkPermission()) {
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

    private boolean checkIfSheetAvailable() {
        return false;
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Needed")
                    .setMessage("This Permission is needed to read file from your storage.")
                    .setPositiveButton("ok", (dialog, which) -> {
                        // Request Permission
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                        dialog.dismiss();
                    }).create().show();
        } else {
            // Request Permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
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
                    List<String> subjects = new ArrayList<String>();


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

                                // Read Teachers
                                cur_row = (HSSFRow) rowIter.next();
                                if( cur_row.getPhysicalNumberOfCells() == 2 && cur_row.getCell(0).toString().equalsIgnoreCase("Teachers")){
                                    teachers_with_courses = Arrays.asList(cur_row.getCell(1).toString().split("|"));
                                }

                                // Read Subjects
                                cur_row = (HSSFRow) rowIter.next();
                                if( cur_row.getPhysicalNumberOfCells() == 2 && cur_row.getCell(0).toString().equalsIgnoreCase("Courses")){
                                    subjects = Arrays.asList( cur_row.getCell(1).toString().split("|"));
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

                    // if valid
                    if (course_readied) {

                        while ( rowIter.hasNext() ) {
                            cur_row = (HSSFRow) rowIter.next();
                            if (cur_row != null && !cur_row.getCell(0).toString().isEmpty()) {
                                if (cur_row.getPhysicalNumberOfCells() == 2 && cur_row.getCell(0).toString().equalsIgnoreCase("roll") && cur_row.getCell(1).toString().equalsIgnoreCase("name")) {

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

                        // Add students
                        for (String student : student_list) {
                            String[] student_data = student.split("/");
                            Timber.i("Debug student list : " + student);
                            viewModel.addStudent(new Student(  Integer.parseInt(student_data[0]), student_data[1],  viewModel.getCourseId( cur_courseReadied )));
                        }

                        // Add Courses
//                            if (students.isEmpty()) {
//                                invalidSheetPopup();
//                            } else {
//                                // insert the student list as course added
//                                viewModel.addCourse(new Course(finalCur_courseReadied, 60));
//                            }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        startActivityForResult(filePickerIntent, READ_REQUEST_CODE);

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
}