package com.passion.attendancesheet.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentAdminSignUpBinding;
import com.passion.attendancesheet.fragments.Congratulation;
import com.passion.attendancesheet.fragments.Sign_in;


public class AdminSignUp extends Fragment {

    FirebaseDatabase db;
    DatabaseReference adminRef;

    FragmentAdminSignUpBinding binding;

    public AdminSignUp() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAdminSignUpBinding.inflate(getLayoutInflater());

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.submitSignUp.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up_animation));

                db = FirebaseDatabase.getInstance();
                adminRef = db.getReference("admin");

                String admin_name, admin_email, admin_passwd, admin_confirm;

                if( (admin_name = binding.editName.getText().toString().trim()).isEmpty() ){
                     binding.editName.setError("Empty Field");
                }
                else if( (admin_email = binding.editEmail.getText().toString().trim()).isEmpty() ){
                    binding.editEmail.setError("Empty Field");
                }
                else if( (admin_passwd = binding.editPasswd.getText().toString().trim()).isEmpty() ){
                    binding.editPasswd.setError("Empty Field");
                }
                else if( !(admin_confirm = binding.editConfirm.getText().toString().trim()).equals( admin_passwd ) ){
                    binding.editConfirm.setError("Password not matched");
                }
                else{

                    // verify validity of email and create user
                    adminRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if( task.isSuccessful() ){

                                String admin_email_reg = task.getResult().child("email").getValue().toString();
                                if( binding.editEmail.getText().toString().trim().equals(admin_email_reg) ){
                                    // eligible for sign up
                                    Toast.makeText(getContext(), "You are eligible for Admin Registration", Toast.LENGTH_LONG ).show();
                                    admin_signUp( admin_email, admin_passwd );
                                }
                                else{
                                    Toast.makeText(getContext(), "You are not eligible for Admin Registration", Toast.LENGTH_LONG ).show();
                                }

                            }
                            else{
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG ).show();
                            }
                        }
                    });

                }

            }
        });

    }


    void admin_signUp( String email, String passwd ){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, passwd )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ){
                            // set admin name
                            adminRef.child("name").setValue(binding.editName.getText().toString().trim() );

                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                        }
                        else{
                            Toast.makeText(getContext(), "Sign up failed", Toast.LENGTH_LONG ).show();
                        }
                    }
                });
    }

    void updateUI( FirebaseUser user ){
        if( user != null ){
            // successful sign up
            Toast.makeText(getContext(), "Successfully Registered", Toast.LENGTH_LONG ).show();

            // open congratulation fragment
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(AdminSignUpDirections.actionAdminSignUpToCongratulation(binding.editName.getText().toString(), getString(R.string.ADMIN)));

        }
    }
}