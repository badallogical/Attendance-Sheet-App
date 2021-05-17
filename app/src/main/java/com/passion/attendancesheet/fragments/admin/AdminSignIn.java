package com.passion.attendancesheet.fragments.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.passion.attendancesheet.AdminActivity;
import com.passion.attendancesheet.LoginActivity;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentAdminSignInBinding;
import com.passion.attendancesheet.databinding.FragmentAdminSignUpBinding;

public class AdminSignIn extends Fragment {

   FragmentAdminSignInBinding binding;

   NavController navController;

    public AdminSignIn() {
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

        binding = FragmentAdminSignInBinding.inflate(getLayoutInflater());

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.submitSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editEmail.getText().toString().trim();
                String passwd = binding.editPasswd.getText().toString().trim();

                if( email.isEmpty() ){
                    binding.editEmail.setError("Field empty");
                }
                else if( passwd.isEmpty() ){
                    binding.editPasswd.setError("Field empty");
                }
                else{

                    adminSignIn( email, passwd );

                }
            }
        });

    }

    void adminSignIn( String email, String passwd ){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword( email, passwd ).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ){
                            // if sign in
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI( currentUser );
                        }
                        else{
                            Toast.makeText(getContext(), "Sign In Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void updateUI( FirebaseUser user ){
        if( user != null ){
             navController.navigate( AdminSignInDirections.actionAdminSignInToAdminActivity(user.getEmail().toString()));
//           Intent intent = new Intent( getContext() , AdminActivity.class );
//           startActivity( intent );

        }
    }
}