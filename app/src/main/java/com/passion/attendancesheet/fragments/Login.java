package com.passion.attendancesheet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passion.attendancesheet.LoginBottomSheet;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentLoginBinding;


public class Login extends Fragment {

    private FragmentLoginBinding binding;
    private FragmentManager parentFM;
    public Login() {

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

        parentFM = getParentFragmentManager();

        binding.signInBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
//
//                fragmentTransaction.replace(R.id.fragment_container_view,Sign_in.class,null)
//                        .addToBackStack(null)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .commit();

                LoginBottomSheet bottomSheet = new LoginBottomSheet(getContext(), 0);
                bottomSheet.show( getParentFragmentManager() , "signInSheet");
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
//
//                fragmentTransaction.replace(R.id.fragment_container_view,Sign_up.class,null)
//                        .addToBackStack(null)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .commit();

                LoginBottomSheet bottomSheet = new LoginBottomSheet(getContext(),1);
                bottomSheet.show( getParentFragmentManager() , "SignUpSheet");

            }
        });
    }
}