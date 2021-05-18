package com.passion.attendancesheet.fragments.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentCourseCRBinding;

public class CourseCR extends Fragment {

    FragmentCourseCRBinding binding;

    public CourseCR() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        NavController navController = NavHostFragment.findNavController( this );

        binding = FragmentCourseCRBinding.inflate(getLayoutInflater());
        CourseCRArgs args = CourseCRArgs.fromBundle( getArguments() );

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