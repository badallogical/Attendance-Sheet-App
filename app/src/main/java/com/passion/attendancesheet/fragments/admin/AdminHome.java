package com.passion.attendancesheet.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.AdminCourseListAdapter;
import com.passion.attendancesheet.databinding.FragmentAdminHomeBinding;

import java.util.ArrayList;

public class AdminHome extends Fragment {

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
            ArrayList<String> array = new ArrayList<String>();
            array.add("BCA-I");
            array.add("BCA-VI");
            array.add("BBA-I");
            array.add("BBA-VI");

            AdminCourseListAdapter adapter = new AdminCourseListAdapter( array );
            binding.courseList.setAdapter(adapter);
            binding.courseList.setLayoutManager( new GridLayoutManager(getContext(), 2));
        }
        else{
            Toast.makeText(getContext(), "Email is not verified", Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onStart() {
        super.onStart();
    }
}