package com.passion.attendancesheet.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentCourseCRBinding;
import com.passion.attendancesheet.dataclasses.ClassRepresentative;

public class CourseCR extends Fragment {

    FragmentCourseCRBinding binding;
    NavController navController;

    public CourseCR() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get NavController
        navController = NavHostFragment.findNavController( this );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        binding = FragmentCourseCRBinding.inflate(getLayoutInflater());

        // set add cr + button background to card background.
        binding.addCr1.setBackground( binding.cr1.getBackground() );
        binding.addCr2.setBackground( binding.cr1.getBackground() );

        // get Args
        CourseCRArgs args = CourseCRArgs.fromBundle( getArguments() );

        ref.child("crs").child(args.getCourse()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if( task.isSuccessful() ){


                            if ( task.getResult().child("cr1").exists() ){
                                ClassRepresentative cr1 = (ClassRepresentative)task.getResult().child("cr1").getValue();

                            }
                        }
                    }
                });

        // add cr 1
        binding.addCr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             navController.navigate( CourseCRDirections.actionCourseCRToAddCR( args.getCourse(), 1 ));
            }
        });

        // add cr 2
        binding.addCr2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               navController.navigate( CourseCRDirections.actionCourseCRToAddCR( args.getCourse(), 2 ));
            }
        });

        return binding.getRoot();
    }
}