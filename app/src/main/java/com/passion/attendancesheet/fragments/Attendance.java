package com.passion.attendancesheet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentAttendanceBinding;

/**
 * It is the panel to take attendance and save, and share  ( as excel )
 */
public class Attendance extends Fragment {

    FragmentAttendanceBinding binding;

    public Attendance() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttendanceBinding.inflate( getLayoutInflater() );
        return binding.getRoot();
    }
}