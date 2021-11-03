package com.passion.attendancesheet.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.StudentListAdapter;
import com.passion.attendancesheet.databinding.FragmentAttendanceBinding;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Attendance;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.utils.Accessory_tool;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

/**
 * It is the panel to take attendance and save, and share  ( as excel )
 */
public class AttendancePanel extends Fragment {

    FragmentAttendanceBinding binding;
    AttendanceSheetViewModel viewModel;
    StudentListAdapter studentListAdapter;
    AttendancePanelArgs args;

    NavController navController;

    String mode;

    static final String STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    static final int PERMISSION_REQUEST_CODE = 1;
    private File file;


    public AttendancePanel() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        viewModel = new ViewModelProvider(this).get(AttendanceSheetViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttendanceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = AttendancePanelArgs.fromBundle(getArguments());

        // Prepare Header
        binding.attCourseName.setText(args.getCourse());
        binding.attLecture.setText(args.getLecture());
        binding.attSubject.setText(args.getSubject());
        binding.attTeacher.setText(args.getTeacher().split(",")[1]);

        mode = args.getMode();


        // Prepare Student List based on mode
        studentListAdapter = new StudentListAdapter(getContext(), new ArrayList<Student>());
        if (args.getMode().equals(getString(R.string.normal))) {
            viewModel.getAllStudent(args.getCourse()).observe(getViewLifecycleOwner(), students -> {
                studentListAdapter.setStudents(students, args.getMode());
            });
        } else if (args.getMode().equals(getString(R.string.edit))) {
            viewModel.getStudentsAttendance(args.getSheetId()).observe(getViewLifecycleOwner(), attendances -> {
                List<Student> studentList = new ArrayList<>();
                for (Attendance a : attendances) {
                    studentList.add(new Student(a.roll_no, a.student_name, args.getCourse()));
                }
                studentListAdapter.setStudentsPresentAsList(studentList, args.getMode());
            });
        }


        binding.studentList.setAdapter(studentListAdapter);
        binding.studentList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Student list swipe Functioning
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

        }).attachToRecyclerView(binding.studentList);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.attendance_sheet_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if (mode.equals(getString(R.string.edit))) {
            menu.findItem(R.id.edit).setVisible(true);
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.send).setVisible(true);
        } else {
            menu.findItem(R.id.edit).setVisible(false);
            menu.findItem(R.id.save).setVisible(true);
            menu.findItem(R.id.send).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                studentListAdapter.setMode(getString(R.string.editToNormal));
                mode = getString(R.string.editToNormal);
                getActivity().invalidateOptionsMenu();
                viewModel.getAllStudent(args.getCourse()).observe(getViewLifecycleOwner(), students -> {
                    studentListAdapter.setStudentsAndPresent(students);
                });

                //navController.navigate( AttendancePanelDirections.actionAttendanceSelf(args.getCourse(), args.getLecture(),args.getSubject(),args.getTeacher(), getString(R.string.editToNormal), args.getSheetId() ));
                break;

            case R.id.send: prepareExcelFileAndSend();
                break;

            case R.id.save:
                saveSheet();
                break;

        }

        return true;
    }

    private void saveSheet() {

        // get Current data and time
        String dateTime = new SimpleDateFormat("MMM dd, yyyy-hh:mm a", Locale.US).format(Calendar.getInstance().getTime());

//        Timber.d( args.getLecture() + " , " + args.getTeacher().split(",")[0]  );

        // create attendance sheet
//        if( mode.equals(getString(R.string.normal)))
//        viewModel.addAttendanceSheet( new Attendance_sheet( studentListAdapter.getStudents().get(0).course_id , dateTime , Accessory_tool.getIntFromRoman(args.getLecture()) , Integer.parseInt( args.getTeacher().split(",")[0] ) , args.getSubject()));

        Attendance_sheet sheet = new Attendance_sheet(studentListAdapter.getStudents().get(0).course_id, dateTime, Accessory_tool.getIntFromRoman(args.getLecture()), new Teacher(Integer.parseInt(args.getTeacher().split(",")[0]) , args.getTeacher().split(",")[1]), args.getSubject());

        // Get Newly inserted sheet id
//        viewModel.getAllSheetsByCourseId( args.getCourse() ).observe( getViewLifecycleOwner(), sheets -> {
//            if( sheets.size() != 0 ){
//                int sheet_id = sheets.get(0).id;
//
//
//
//                if( studentPresent.size() == 0 ){
//                    Timber.d("student present 0 ");
//                }
//                Timber.d("student present " + studentPresent.size());
//
//                for( Student s : studentPresent ){
//                    viewModel.addAttendance( new Attendance(sheet_id, s.roll_number, s.name, "Present") );
//                }
//
//                // Update Current Attendance Sheet
//                viewModel.updateSheetTotalPresentAndAbsent( sheet_id, studentPresent.size(), studentListAdapter.getStudents().size() - studentPresent.size() );
//
//                // remove attendance ( if removed in edit )
//                for( Student s : studentAbsent ){
//                    viewModel.removeAttendance( new Attendance(sheet_id, s.roll_number, s.name, "Present"));
//                }
//
//
//            }
        // save all attendance now
        List<Student> studentPresent = studentListAdapter.getStudentPresent();
        List<Student> studentAbsent = studentListAdapter.getStudentMarkedAbsent();
        viewModel.saveAttendanceSheetWithAttendance(sheet, studentPresent, studentAbsent, mode);
        navController.navigate(AttendancePanelDirections.actionAttendanceToDashboard());

    }

    public static boolean isExternalStorageWritable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    public boolean prepareExcelFileAndSend() {

        if (!isExternalStorageWritable() || isExternalStorageReadOnly()) {
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
            c.setCellValue(args.getCourse().split("-")[0]);
            c.setCellStyle(cellStyle);

            c = row.createCell(2);
            c.setCellValue(args.getCourse().split("-")[1]);
            c.setCellStyle(cs);


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

            List<Student> stu_presents = studentListAdapter.getStudentPresent();
            for (int i = 0; i < stu_presents.size(); i++) {
                // add to sheet
                row = sheet1.createRow(row.getRowNum() + 1);
                c = row.createCell(0);
                c.setCellValue(i+1);
                c.setCellStyle( cellStyle );

                c = row.createCell(1);
                c.setCellValue(stu_presents.get(i).roll_number);
                c.setCellStyle( cellStyle );

                c = row.createCell(2);
                c.setCellValue(Accessory_tool.convertToCamelCase( stu_presents.get(i).name ) );
                cellStyle.setWrapText(true);
            }
//            }

            String dateTime = new SimpleDateFormat("MMM dd, yyyy-hh:mm a", Locale.US).format(Calendar.getInstance().getTime());
            file = new File( getContext().getExternalFilesDir(null) , args.getCourse() + " " + "L-" + args.getLecture() + " " + dateTime.split(",")[0] + ".xls");
            try (FileOutputStream os = new FileOutputStream(file)) {
                wb.write(os);


                Timber.tag("FileUtils").w("Writing file " + file);

                success = true;
            } catch (IOException  e) {
                Timber.tag("fileUtiles").w(e, "Error writing" + file);
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
                new AlertDialog.Builder(getContext())
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
        int result = ContextCompat.checkSelfPermission( getContext() , STORAGE_PERMISSION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermission() {
        if( ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),STORAGE_PERMISSION )){

            // Alert Dialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed for sharing file to other apps")
                    .setPositiveButton("ok", (dialog, which) -> requestPermissions( new String[] {STORAGE_PERMISSION}, PERMISSION_REQUEST_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss()).create().show();
        }
        else{
           requestPermissions( new String[] { STORAGE_PERMISSION }, PERMISSION_REQUEST_CODE );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(getContext(), "Permission Successfull", Toast.LENGTH_SHORT).show();
                sendFile(file );
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean sendFile(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, file.getName());
        try {
            if (file.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri path = FileProvider.getUriForFile(getContext(), "com.passion.attendancesheet", file);
                    intent.putExtra(Intent.EXTRA_STREAM, path);
                } else {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("plain/*");
                startActivity(intent);
            }
        } catch (Exception e) {
            Timber.d(Objects.requireNonNull(e.getMessage()));
        }
        return true;
    }
}