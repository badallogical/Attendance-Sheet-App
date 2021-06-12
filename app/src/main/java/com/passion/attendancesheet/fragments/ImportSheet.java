package com.passion.attendancesheet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passion.attendancesheet.R;


public class ImportSheet extends Fragment {


    public ImportSheet() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // if sheet already imported, redirect to admin home
        if( checkIfSheetAvailable() == true ){
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(ImportSheetDirections.actionImportSheetToHome());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_import_sheet, container, false);
    }

    private boolean checkIfSheetAvailable() {
        return false;
    }
}