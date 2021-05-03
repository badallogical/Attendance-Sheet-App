package com.passion.attendancesheet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.passion.attendancesheet.databinding.BottomSheetLayoutBinding;
import com.passion.attendancesheet.fragments.Sign_in;
import com.passion.attendancesheet.fragments.Sign_up;
import com.passion.attendancesheet.fragments.admin.AdminSignIn;
import com.passion.attendancesheet.fragments.admin.AdminSignUp;

public class LoginBottomSheet extends BottomSheetDialogFragment {

    int commingFrom;        // need to know how to communicate with bottom sheet dailog

    BottomSheetLayoutBinding binding;

    Context context;

    public LoginBottomSheet( Context context , int commingFrom){
        this.context = context;
        this.commingFrom = commingFrom;
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
                if( commingFrom == 0 ){
                    // sign in
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_container_view, Sign_in.class, null)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();

                }
                else{
                    // sign in
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_container_view, Sign_up.class, null)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }

                // close the sheet
                dismiss();
           }


       });

        // I am admin
        binding.iAmAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( commingFrom == 0 ){
                    // sign in
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_view, AdminSignIn.class, null)
                            .addToBackStack(null)   // to change the login option
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }
                else{
                    // sign up
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_view, AdminSignUp.class, null )
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }

                // close the sheet
                dismiss();
            }
        });

    }
}
