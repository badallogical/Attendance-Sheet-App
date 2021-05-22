package com.passion.attendancesheet.fragments.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.AdminCourseListAdapter;
import com.passion.attendancesheet.adapters.CourseListClick;
import com.passion.attendancesheet.databinding.FragmentAdminHomeBinding;

import java.util.ArrayList;

public class AdminHome extends Fragment implements CourseListClick {

    FragmentAdminHomeBinding binding;

    public AdminHome() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAdminHomeBinding.inflate(getLayoutInflater());

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if( currentUser.isEmailVerified() ){

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference();

            // TODO : fetch data from firebase
           ArrayList<String > array = new ArrayList<>();
           array.add("BCA");
           array.add("BSC");

            AdminCourseListAdapter adapter = new AdminCourseListAdapter( array , getContext() , this );
            binding.courseList.setAdapter(adapter);
            binding.courseList.setLayoutManager( new GridLayoutManager(getContext(), 2 ) );
        }
        else{
            Toast.makeText(getContext(), "Email is not verified", Toast.LENGTH_LONG).show();
        }

        binding.addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                        View custom_layout = LayoutInflater.from(getContext()).inflate(R.layout.add_course_dialog, null );

                        Spinner semesterSpinner = custom_layout.findViewById(R.id.semester_spinner);
                        ArrayList<String> semester = new ArrayList<>();
                        semester.add("I");
                        semester.add("II");
                        semester.add("III");
                        semester.add("IV");
                        semester.add("V");
                        semester.add("VI");


                        semesterSpinner.setAdapter( new ArrayAdapter<String>( getContext(), android.R.layout.simple_spinner_dropdown_item, semester ){
                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View  v = super.getDropDownView(position, convertView, parent);
                                ((TextView)v).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                return v;
                            }
                        });

                        // create and show dialog
                        dialogBuilder.setView( custom_layout )
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO:
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = db.getReference("courses");
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("Add Course")
                                .create().show();

                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void openCrPanel(String course ) {
        NavHostFragment.findNavController(this).navigate( AdminHomeDirections.actionAdminHomeToCourseCR( course ));
    }
}