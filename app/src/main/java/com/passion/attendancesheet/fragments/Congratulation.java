package com.passion.attendancesheet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentCongratulationBinding;
import com.passion.attendancesheet.fragments.admin.AdminSignIn;

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

        Bundle args = getArguments();
        String userType = args.getString("user");


        if( userType == "admin"){
            binding.congratesText.setText("You are now a Administrator");
        }
        else{
            binding.congratesText.setText("You are now a Class Representative");
        }

        binding.signInBtn.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if( userType == "admin"){


                    // verify user email
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if( task.isSuccessful() ){

                                Toast.makeText(getContext(), "verification is send to your email", Toast.LENGTH_LONG).show();

                                getParentFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container_view, AdminSignIn.class, null)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .commit();
                            }
                            else{
                                Toast.makeText(getContext(), "Email verified" + currentUser.isEmailVerified() , Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
                else{
                    getParentFragmentManager().beginTransaction()
                            .add(R.id.fragment_container_view, Sign_in.class, null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }

            }
        });
    }
}