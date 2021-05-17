package com.passion.attendancesheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.passion.attendancesheet.databinding.BottomSheetLayoutBinding;

public class LoginBottomSheet extends BottomSheetDialogFragment {

    int comingFrom;

    BottomSheetLayoutBinding binding;

    NavController navController;

    public LoginBottomSheet( NavController navController , int comingFrom){
        this.navController = navController;
        this.comingFrom = comingFrom;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetLayoutBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // I am cr
        binding.iAmCr.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                if( comingFrom == 0 ){
                    // sign in
                    navController.navigate( R.id.action_login_to_sign_in);

                }
                else{
                    // sign up
                    navController.navigate( R.id.action_login_to_sign_up);
                }

                // close the sheet
                dismiss();
           }


       });

        // I am admin
        binding.iAmAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( comingFrom == 0 ){
                    // sign in
                    navController.navigate( R.id.action_login_to_adminSignIn);
                }
                else{
                    // sign up
                    navController.navigate(R.id.action_login_to_adminSignUp);
                }

                // close the sheet
                dismiss();
            }
        });

    }
}
