package com.passion.attendancesheet.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentAddCRBinding;


public class AddCR extends Fragment {


    FragmentAddCRBinding binding;

    public AddCR() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddCRBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = getArguments();
                if( Integer.parseInt(args.get("CR").toString()) == 1 ){

                    // add cr 1 to the firebase if exist warn and replace
                    Toast.makeText(getContext(), "CR 1", Toast.LENGTH_LONG).show();

                }
                else{

                    // add cr 2 to the firebase if exist warn and replace
                    Toast.makeText(getContext(), "CR 2", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}