package com.passion.attendancesheet.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentAddCRBinding;
import com.passion.attendancesheet.dataclasses.ClassRepresentative;


public class AddCR extends Fragment {


    FragmentAddCRBinding binding;
    FirebaseDatabase db;
    NavController navController;

    public AddCR() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController( this );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddCRBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        // Display cr data if exist
        AddCRArgs args = AddCRArgs.fromBundle( getArguments() );
        if( args.getCr() != null ){
           binding.editName.setText( args.getCr().getName());
           binding.editEmail.setText( args.getCr().getEmail());
        }

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCRArgs args = AddCRArgs.fromBundle( getArguments() );

                ClassRepresentative crs[] = {null,null};

                // Check fields
                if( binding.editName.getText().toString().trim().isEmpty() ){
                    binding.editName.setError(" Field Empty ");
                }
                else if( binding.editEmail.getText().toString().trim().isEmpty() ){
                    binding.editEmail.setError( " Field Empty ");
                }
                else{
                    if( args.getCrNo() == 1 ){

                        // Add cr 1 to the firebase if exist warn and replace
                        ref.child("crs").child(args.getCourse()).child("cr1").setValue(  new ClassRepresentative(binding.editName.getText().toString(), binding.editEmail.getText().toString()) );
                        Toast.makeText( getContext(), "CR 1 Registered Successfully", Toast.LENGTH_LONG).show();
                        navController.navigateUp();
                    }
                    else{

                        // Add cr 2 to the firebase if exist warn and replace
                        ref.child("crs").child(args.getCourse()).child("cr2").setValue( new ClassRepresentative(binding.editName.getText().toString(), binding.editEmail.getText().toString()) );
                        Toast.makeText( getContext(), "CR 2 Registered Successfully", Toast.LENGTH_LONG).show();
                        navController.navigateUp();
                    }
                }

            }
        });

    }
}