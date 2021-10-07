package com.passion.attendancesheet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
    NavController navController;

    public Congratulation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);

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

        CongratulationArgs args = CongratulationArgs.fromBundle( getArguments() );
        String userName = args.getUserName();
        userName = String.valueOf(Character.toUpperCase(userName.charAt(0) )) + userName.substring(1);

        if( args.getUserType().equals(getString(R.string.ADMIN))){
            binding.congratesText.setText( userName + ", You are now a Administrator");
        }
        else{
            binding.congratesText.setText(userName.toString() + ", You are now a Class Representative");

        }

        // Redirect to sign in screen accordingly based on type of user
        binding.verifySignInBtn.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if( args.getUserType().equals(getString(R.string.ADMIN))){


                    // verify user email
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if( task.isSuccessful() ){

                                Toast.makeText(getContext(), "Verification link is send to your email", Toast.LENGTH_LONG).show();

                                // navigate to Admin Sign IN
                                navController.navigate( CongratulationDirections.actionCongratulationToAdminSignIn());
                            }
                            else{
                                Toast.makeText(getContext(), "Failed to send email verification " , Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
                else{

                    // navigate to CR Sign IN ( without email verification )
                    navController.navigate( CongratulationDirections.actionCongratulationToSignIn2());

                }

            }
        });
    }
}