package com.passion.attendancesheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.passion.attendancesheet.databinding.LoginScreenBinding;
import com.passion.attendancesheet.databinding.LoginScreenFragmentBinding;

public class LoginActivity extends AppCompatActivity {


    private LoginScreenFragmentBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LoginScreenFragmentBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );


        fragmentManager = getSupportFragmentManager();


        binding.signInButton.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.)
            }
        });
    }



}