package com.passion.attendancesheet.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    CourseCRArgs args;

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



        binding = FragmentCourseCRBinding.inflate(getLayoutInflater());

        // set add cr + button background to card background.
        binding.addCr1.setBackground( binding.cr1Card.getBackground() );
        binding.addCr2.setBackground( binding.cr1Card.getBackground() );

        // get Args
        args = CourseCRArgs.fromBundle( getArguments() );

        // add cr 1
        binding.addCr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             navController.navigate( CourseCRDirections.actionCourseCRToAddCR( args.getCourse(), 1 , null));
            }
        });

        // add cr 2
        binding.addCr2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               navController.navigate( CourseCRDirections.actionCourseCRToAddCR( args.getCourse(), 2 , null));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch data for course cr if available
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        ref.child("crs").child(args.getCourse()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if( task.isSuccessful() ){

                            binding.cr1Progressbar.setVisibility(View.GONE);
                            binding.cr2Progressbar.setVisibility(View.GONE);

                            if ( task.getResult().child("cr1").exists() ){
                                ClassRepresentative cr1 = task.getResult().child("cr1").getValue(ClassRepresentative.class);
                                binding.addCr1.setVisibility(View.GONE);
                                binding.cr1.setVisibility(View.VISIBLE);
                                binding.cr1Name.setText( cr1.getName() );
                                binding.cr1Email.setText( cr1.getEmail() );

                                binding.cr1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        navController.navigate( CourseCRDirections.actionCourseCRToAddCR( args.getCourse(), 1, cr1));
                                    }
                                });
                            }
                            else{
                                binding.cr1.setVisibility(View.GONE);
                                binding.addCr1.setVisibility(View.VISIBLE);
                            }

                            if( task.getResult().child("cr2").exists() ){
                                ClassRepresentative cr2 = task.getResult().child("cr2").getValue(ClassRepresentative.class);
                                binding.addCr2.setVisibility(View.GONE);
                                binding.cr2.setVisibility(View.VISIBLE);
                                binding.cr2Name.setText( cr2.getName() );
                                binding.cr2Email.setText( cr2.getEmail() );

                                binding.cr2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        navController.navigate( CourseCRDirections.actionCourseCRToAddCR( args.getCourse(), 2, cr2));
                                    }
                                });
                            }
                            else{
                                binding.cr2.setVisibility(View.GONE);
                                binding.addCr2.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            binding.cr1Progressbar.setVisibility(View.GONE);
                            binding.cr2Progressbar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Something went wrong" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}