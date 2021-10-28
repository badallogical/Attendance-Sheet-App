package com.passion.attendancesheet.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.compose.ui.graphics.Paint;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.StudentListAdapter;
import com.passion.attendancesheet.databinding.FragmentAttendanceBinding;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.model.entity.Student;
import com.passion.attendancesheet.model.entity.Attendance;
import com.passion.attendancesheet.utils.Accessory_tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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



    public AttendancePanel() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        viewModel = new ViewModelProvider(this).get( AttendanceSheetViewModel.class );
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttendanceBinding.inflate( getLayoutInflater() );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = AttendancePanelArgs.fromBundle( getArguments() );

        // Prepare Header
        binding.courseName.setText(args.getCourse());
        binding.lecture.setText(args.getLecture());
        binding.subject.setText(args.getSubject());
        binding.teacher.setText(args.getTeacher().split(",")[1]);

        mode = args.getMode();

        // Prepare Student List based on mode
        studentListAdapter = new StudentListAdapter(getContext(), new ArrayList<Student>());
        if( args.getMode().equals(getString(R.string.normal))) {
            viewModel.getAllStudent(args.getCourse()).observe(getViewLifecycleOwner(), students -> {
                studentListAdapter.setStudents(students, args.getMode());
            });
        }else if( args.getMode().equals(getString(R.string.edit))){
            viewModel.getStudentsAttendance(args.getSheetId()).observe( getViewLifecycleOwner(), attendances -> {
                    List<Student> studentList = new ArrayList<>();
                    for( Attendance a : attendances ){
                        studentList.add( new Student(a.roll_no, a.student_name, args.getCourse()));
                    }
                    studentListAdapter.setStudentsPresentAsList( studentList , args.getMode() );
                });
        }



        binding.studentList.setAdapter(studentListAdapter);
        binding.studentList.setLayoutManager( new LinearLayoutManager(getContext()));

        // Student list swipe Functioning
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

        }).attachToRecyclerView( binding.studentList );

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.attendance_sheet_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if( mode.equals(getString(R.string.edit))){
            menu.findItem(R.id.edit).setVisible(true);
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.send).setVisible(true);
        }
        else{
            menu.findItem(R.id.edit).setVisible(false);
            menu.findItem(R.id.save).setVisible(true);
            menu.findItem(R.id.send).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId() ){
            case R.id.edit:
                studentListAdapter.setMode( getString(R.string.editToNormal ));
                mode = getString(R.string.editToNormal );
                getActivity().invalidateOptionsMenu();
                viewModel.getAllStudent( args.getCourse() ).observe( getViewLifecycleOwner(), students -> {
                    studentListAdapter.setStudentsAndPresent(students);
                });

                //navController.navigate( AttendancePanelDirections.actionAttendanceSelf(args.getCourse(), args.getLecture(),args.getSubject(),args.getTeacher(), getString(R.string.editToNormal), args.getSheetId() ));
                                break;

            case R.id.send :    break;

            case R.id.save:     saveSheet();
                                break;

        }

        return true;
    }

    private void saveSheet() {

        // get Current data and time
        String dateTime = new SimpleDateFormat("MMM dd, yyyy-hh:mm a", Locale.US).format(Calendar.getInstance().getTime());

        Timber.d( args.getLecture() + " , " + args.getTeacher().split(",")[0]  );

        // create attendance sheet
        if( mode.equals(getString(R.string.normal)))
        viewModel.addAttendanceSheet( new Attendance_sheet( studentListAdapter.getStudents().get(0).course_id , dateTime , Accessory_tool.getIntFromRoman(args.getLecture()) , Integer.parseInt( args.getTeacher().split(",")[0] ) , args.getSubject()));


        // Get Newly inserted sheet id
        viewModel.getAllSheetsByCourseId( args.getCourse() ).observe( getViewLifecycleOwner(), sheets -> {
            if( sheets.size() != 0 ){
                int sheet_id = sheets.get(0).id;

                // save all attendance now
                List<Student> studentPresent = studentListAdapter.getStudentPresent();
                List<Student> studentAbsent = studentListAdapter.getStudentAbsent();
                for( Student s : studentPresent ){
                    viewModel.addAttendance( new Attendance(sheet_id, s.roll_number, s.name, "Present") );
                }

                // remove attendance ( if removed in edit )
                for( Student s : studentAbsent ){
                    viewModel.removeAttendance( new Attendance(sheet_id, s.roll_number, s.name, "Present"));
                }


            }

            navController.navigate(AttendancePanelDirections.actionAttendanceToDashboard());

        });

    }
}