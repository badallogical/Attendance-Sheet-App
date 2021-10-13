package com.passion.attendancesheet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.passion.attendancesheet.LoginBottomSheet;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentLoginBinding;


public class Login extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        NavController navController = NavHostFragment.findNavController(this);

        if( currentUser != null  ){
            currentUser.reload();

            if(currentUser.getEmail().equals("0i0am1a1programmer@gmail.com"))
                navController.navigate( LoginDirections.actionLoginToAdminActivity( currentUser.getEmail().toString() ));
            else
                navController.navigate( LoginDirections.actionLoginToNavigation() );
        }
        else{
            Toast.makeText(getContext(), "NO current User", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the navController using fragment
        NavController navController = NavHostFragment.findNavController( this );

        // Sign In
        binding.signInBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                LoginBottomSheet bottomSheet = new LoginBottomSheet( navController , 0);
                bottomSheet.show( getParentFragmentManager() , "signInSheet");
            }
        });

        // Sign - Up
        binding.signUpBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                LoginBottomSheet bottomSheet = new LoginBottomSheet(navController,1);
                bottomSheet.show( getParentFragmentManager() , "SignUpSheet");

            }
        });
    }
}