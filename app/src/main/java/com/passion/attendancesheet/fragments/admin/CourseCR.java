package com.passion.attendancesheet.fragments.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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


        binding = FragmentCourseCRBinding.inflate(getLayoutInflater());

        // add cr 1
        binding.addCr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putInt("CR", 1);

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.admin_fragment_container, AddCR.class, args)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        // add cr 2
        binding.addCr2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putInt("CR", 2);

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.admin_fragment_container, AddCR.class, args)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        return binding.getRoot();
    }
}