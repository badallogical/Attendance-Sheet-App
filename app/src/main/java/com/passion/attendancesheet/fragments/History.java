package com.passion.attendancesheet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.passion.attendancesheet.databinding.FragmentHistoryBinding;


/**
 * Display the attendance sheets take so far
 */
public class History extends Fragment {


    FragmentHistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate( getLayoutInflater() );
        return binding.getRoot();
    }

}
