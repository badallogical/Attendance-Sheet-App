package com.passion.attendancesheet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passion.attendancesheet.databinding.FragmentHistoryBinding;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;
import com.passion.attendancesheet.utils.Accessory_tool;

import timber.log.Timber;


/**
 * Display the attendance sheets take so far
 */
public class History extends Fragment {


    FragmentHistoryBinding binding;
    AttendanceSheetViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate( getLayoutInflater() );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // Find the current course for a respective CR from firebase

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        currentUser.reload();
        db.getReference().child("crs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String courseId = null;
                for( DataSnapshot courses : snapshot.getChildren() ){
                    for( DataSnapshot crs : courses.getChildren() ){
                        if(crs.child("email").getValue(String.class).equals(currentUser.getEmail())){
                            courseId = courses.getKey();
                            courseId = courseId.split(" ")[0] + "-" + Accessory_tool.getIntFromRoman(courseId.split(" ")[1]);

                            Timber.d("Course ID readed : " + courseId);
                            break;
                        }
                    }

                    if( courseId != null ){
                        break;
                    }
                }

                viewModel = new ViewModelProvider(History.this).get( AttendanceSheetViewModel.class );
                viewModel.getAllSheetsByCourseId(courseId).observe( getViewLifecycleOwner(), sheets -> {
                    // setup adapter
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
