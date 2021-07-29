package com.passion.attendancesheet.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

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
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentHomeBinding;
import com.passion.attendancesheet.fragments.admin.AdminHomeDirections;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * It is the CR home Screen that display the course sheet
 */
public class Home extends Fragment {


    FragmentHomeBinding binding;
    AttendanceSheetViewModel viewModel;

    NavController navController;

    public Home() {
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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

        binding.courseName.setText("BBA 5");
        binding.courseStrength.setText("Total Strength : 60");

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
                List<String> tempTeacher = new ArrayList<>();
                tempTeacher.add("Santosh Sir");
                tempTeacher.add("Rohit Sir");
                ArrayAdapter<String> teacherSpinnerAdapter = new ArrayAdapter<String>( getContext(), android.R.layout.simple_spinner_item, tempTeacher ); // Set DropDown data item view
                teacherSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item); // Set DropDown item View
                teacherSpinner.setAdapter(teacherSpinnerAdapter);

                Spinner subjectSpinner = dialogView.findViewById(R.id.subject_spinner);
                List<String> tempSubject = new ArrayList<>();
                tempSubject.add("Cryptography");
                tempSubject.add("Mobile computing");
                tempSubject.add("Cyber law");
                tempSubject.add("E-Commerce");
                ArrayAdapter<String> subjectSpinnerAdapter = new ArrayAdapter<String>( getContext(), android.R.layout.simple_spinner_item, tempSubject );
                subjectSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                subjectSpinner.setAdapter( subjectSpinnerAdapter);

                Spinner LectureSpinner = dialogView.findViewById(R.id.lecture_spinner);

                // create prepare sheet dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Prepare Sheet");
                builder.setView( dialogView );
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navController.navigate( HomeDirections.actionHomeToAttendance("BBA 5", LectureSpinner.getSelectedItem().toString(),subjectSpinner.getSelectedItem().toString(), teacherSpinner.getSelectedItem().toString() ));
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.cr_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.action_Home_to_login3 ){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText( getContext(), "Signed Out", Toast.LENGTH_LONG).show();
            navController.navigate( R.id.action_Home_to_login3 );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}