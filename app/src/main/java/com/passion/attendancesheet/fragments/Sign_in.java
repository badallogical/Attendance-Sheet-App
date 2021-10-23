package com.passion.attendancesheet.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentImportSheetBinding;
import com.passion.attendancesheet.databinding.FragmentSignInBinding;


public class Sign_in extends Fragment {

    private FragmentSignInBinding binding;

    private FirebaseAuth mAuth;

    public Sign_in() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignInBinding.inflate( getLayoutInflater() );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.submitSignIn.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up_animation));

                String email = binding.editTextTextEmailAddress.getText().toString();
                String password = binding.editTextTextPassword.getText().toString();

                if( email.trim().isEmpty()){
                    binding.editTextTextEmailAddress.setError("Empty Field");
                }
                else if( password.trim().isEmpty()){
                    binding.editTextTextPassword.setError("Empty Field");
                }
                else{
                    signIn( email, password);
                }


            }
        });
    }

    private void signIn(String email, String password ){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            updateUI(firebaseUser);
                        }
                        else{
                            Toast.makeText( getContext(), "Sign In Faild , Invalid Creadentials", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user ){
        if( user != null ){
            Toast.makeText(getContext(), "Successfully Signed In", Toast.LENGTH_LONG).show();

            NavController navController = NavHostFragment.findNavController(this);

            // Open import attendance where if already imported if will redirect to Admin Home
            navController.navigate( Sign_inDirections.actionSignInToNavigation());

        }
        else{
            Toast.makeText(getContext(), "Sign In Failed", Toast.LENGTH_LONG).show();
        }
    }


}