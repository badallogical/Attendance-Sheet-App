package com.passion.attendancesheet.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentHomeBinding;
import com.passion.attendancesheet.fragments.admin.AdminHomeDirections;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;
import com.passion.attendancesheet.model.entity.Teacher;
import com.passion.attendancesheet.model.entity.views.TeacherAndCoursesView;
import com.passion.attendancesheet.utils.Accessory_tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * It is the CR home Screen that display the course sheet
 */
public class Home extends Fragment {


    FragmentHomeBinding binding;
    AttendanceSheetViewModel viewModel;


    NavController navController;
    String courseId = null;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Home() {

    }

    @Override
    public void onStart() {
        super.onStart();
        checkIfSheetAvailable("BBA-5");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get( AttendanceSheetViewModel.class );
        navController = NavHostFragment.findNavController(this);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the current course for a respective CR from firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();

        currentUser.reload();
        db.getReference().child("crs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for( DataSnapshot courses : snapshot.getChildren() ) {
                    for (DataSnapshot crs : courses.getChildren()) {
                        if (crs.child("email").getValue(String.class).equals(currentUser.getEmail())) {
                            courseId = courses.getKey();
                            courseId = courseId.split(" ")[0] + "-" + Accessory_tool.getIntFromRoman(courseId.split(" ")[1]);
                            binding.courseName.setText(courseId);
                            Timber.d("Course ID readed : " + courseId);
                            break;
                        }
                    }

                    if (courseId != null) {
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if( binding.todayList.getChildCount() == 0 ){
            binding.noLecture.setVisibility( View.VISIBLE );
            binding.todayList.setVisibility( View.GONE );
        }
        else{
            binding.noLecture.setVisibility( View.GONE );
            binding.todayList.setVisibility( View.VISIBLE );
        }

        binding.buttonMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation( getContext(), R.anim.pop_up_animation ));

                // setup prepare sheet dialog element
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.prepare_sheet_dialog, null);

                Spinner teacherSpinner = dialogView.findViewById(R.id.teacher_spinner);


                Map<String, String> teacher_id_map = new HashMap<String, String>();
                viewModel.getCourseTeacher(courseId).observe(getViewLifecycleOwner(), teachers -> {


                    // setup adapter
                    List<String> teacher_name = new ArrayList<>();
                    for( TeacherAndCoursesView t : teachers ){
                        teacher_name.add( t.teacher_name );
                        teacher_id_map.put(t.teacher_name, t.teacher_id + "," + t.teacher_name );
                    }

                    ArrayAdapter<String> teacherSpinnerAdapter = new ArrayAdapter<String>( getContext(), android.R.layout.simple_spinner_item , teacher_name); // Set DropDown data item view
                    teacherSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item); // Set DropDown item View
                    teacherSpinner.setAdapter(teacherSpinnerAdapter);

                });


                Spinner subjectSpinner = dialogView.findViewById(R.id.subject_spinner);


                viewModel.getCourseSubject(courseId).observe(getViewLifecycleOwner(), subjects -> {

                    // setup adapter
                    if( subjects != null ) {
                        Timber.i( "Subjects : " + subjects );
                        ArrayAdapter<String> subjectSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subjects.split("\\|"));
                        subjectSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subjectSpinner.setAdapter(subjectSpinnerAdapter);
                    }
                });


                Spinner LectureSpinner = dialogView.findViewById(R.id.lecture_spinner);

                // Create prepare sheet dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Prepare Sheet");
                builder.setView( dialogView );
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navController.navigate( DashboardDirections.actionDashboardToAttendance(courseId, LectureSpinner.getSelectedItem().toString(),subjectSpinner.getSelectedItem().toString(), teacher_id_map.get( teacherSpinner.getSelectedItem().toString()) , getString(R.string.normal) ,-1));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });


    }



    private void checkIfSheetAvailable( String course_name ) {

        // Navigate to CR Home is sheet available
        viewModel.getStudentCount(course_name).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if( integer == 0 ){
                    navController.navigate( DashboardDirections.actionDashboardToImportSheet() );
                    Toast.makeText(getContext(), "No Sheet found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}