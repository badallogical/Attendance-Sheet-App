package com.passion.attendancesheet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentCongratulationBinding;

public class Congratulation extends Fragment {

    FragmentCongratulationBinding binding;

    public Congratulation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCongratulationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signInBtn.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .add(R.id.fragment_container_view, Sign_in.class, null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
    }
}