package com.passion.attendancesheet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentHomeBinding;
import com.passion.attendancesheet.fragments.admin.AdminHomeDirections;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;

/**
 * It is the CR home Screen that display the course sheet
 */
public class Home extends Fragment {


    FragmentHomeBinding binding;
    AttendanceSheetViewModel viewModel;

    public Home() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get( AttendanceSheetViewModel.class );

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.courseName.setText("BBA 5");
        binding.courseStrength.setText("Total Strength : 60");



    }
}