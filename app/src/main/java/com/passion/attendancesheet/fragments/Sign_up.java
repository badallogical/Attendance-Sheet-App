package com.passion.attendancesheet.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentSignUpBinding;
import com.passion.attendancesheet.dataclasses.ClassRepresentative;
import com.passion.attendancesheet.dataclasses.CourseF;
import com.passion.attendancesheet.dataclasses.StudentF;
import com.passion.attendancesheet.dataclasses.UserF;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import timber.log.Timber;

public class Sign_up extends Fragment {

    private FragmentSignUpBinding binding;

    private FirebaseDatabase database;

    private FirebaseAuth mAuth;

    private Context context;


    public Sign_up() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        mAuth = FirebaseAuth.getInstance();

//        // Realtime Database
//        database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("courses");
//
//        // Firebase Authentication
//        mAuth = FirebaseAuth.getInstance();
//
//
//        ArrayList<CourseF> courses;
//        ArrayList<ClassRepresentative> crs;
//        ArrayList<StudentF> students;
//        ArrayList<String> teachers;
//
//        // BCA-6
//
//        // Crs
//        crs = new ArrayList<ClassRepresentative>();
//        crs.add( new ClassRepresentative("Badal", "badallearn@gmail.com", "BCA-6));
//        crs.add( new ClassRepresentative("Shubhum Kumar", "shubhumkumar@gmail.com", "BCA-6"));
//
//
//        // Students
//        students = new ArrayList<StudentF>();
//        students.add( new StudentF("Badal",1));
//        students.add( new StudentF("Badal",2));
//        students.add( new StudentF("Badal",3));
//        students.add( new StudentF("Badal",4));
//        students.add( new StudentF("Badal",5));
//        students.add( new StudentF("Badal",6));
//        students.add( new StudentF("Badal",7));
//        students.add( new StudentF("Badal",8));
//        students.add( new StudentF("Badal",9));
//        students.add( new StudentF("Badal",10));
//        students.add( new StudentF("Badal",11));
//        students.add( new StudentF("Badal",12));
//        students.add( new StudentF("Badal",13));
//
//
//        // teachers
//        teachers = new ArrayList<String>();
//        teachers.add( "Rohit Sir");
//        teachers.add( "Santosh Sir");
//        teachers.add( "Ajay Sir");
//        teachers.add( "L.S Awasthi Sir");
//
//        // add BCA-6
//        courses = new ArrayList<CourseF>();
//        courses.add( new CourseF("BCA-6", 94, crs, students,teachers));
//
//        //save course to db
//        myRef.setValue(courses);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate( getLayoutInflater() );
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // course spinner
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        List<String> courses = new ArrayList<String>();
        ArrayAdapter courseAdapter = new ArrayAdapter( getActivity(),android.R.layout.simple_list_item_1, courses );

        ref.child("crs").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    courses.add( dataSnapshot.getKey() );
                }
                courseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.spinner.setAdapter( courseAdapter );

        // submit action
        binding.submitSignUp.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up_animation));
                if( courses.size() == 0 ){
                    Toast.makeText(getContext(), "No Course Available Yet, Contact Admin", Toast.LENGTH_LONG).show();
                }

                // read data
                String email = binding.editEmail.getText().toString();
                String passwd = binding.editPasswd.getText().toString();

                if( email.trim().isEmpty() ){
                    binding.editEmail.setError("Empty Field");
                }
                else if( passwd.trim().isEmpty()){
                    binding.editPasswd.setError("Empty Field");
                }
                else{
                    // create account
                    signUp(email,passwd);
                }
            }
        });

    }

    public void createAccount( String email, String passwd ){

        mAuth.createUserWithEmailAndPassword( email, passwd )
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ){
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            // write user details to firebase
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference ref = db.getReference();
                            updateUI(currentUser);
                        }
                        else{
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signUp( String email, String passwd ){
        //TODO : implement verification of eligible email only
        if( binding.spinner.getSelectedItem() == null ){
            Toast.makeText(getContext(), "Please Select Your Course", Toast.LENGTH_LONG).show();
        }
        else{

            if( binding.spinner.getSelectedItem().toString().trim().isEmpty() ){
                Toast.makeText( getContext(), "Please Select the Course", Toast.LENGTH_LONG).show();
            }
            else{

                String course = binding.spinner.getSelectedItem().toString();

                // verify the cr credential at firebase , at given course and if verified create account
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference();

                ref.child("crs").child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        boolean verified = false;

                        if( task.isSuccessful() ){
                            // check the email if exist
                            if( task.getResult().child("cr1").exists() ){
                                if( task.getResult().child("cr1").child("email").getValue().equals(email) ){
                                    // verified
                                    verified = true;
                                    createAccount(email,passwd);
                                }
                            }

                            if( task.getResult().child("cr2").exists() ){
                                if( task.getResult().child("cr2").child("email").getValue().equals(email)){
                                    // verified
                                    verified = true;
                                    if( !checkIfExisted( email , passwd ))
                                        createAccount(email,passwd);
                                }
                            }

                            if( !verified ){
                                // not eligible
                                Toast.makeText(getContext(), "You are not eligible", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
            }
        }
    }

    private boolean checkIfExisted(String email, String passwd) {
       return false;
    }

    private void updateUI(FirebaseUser user ){
        if( user != null ){
            Toast.makeText(context, "User created Successfully", Toast.LENGTH_LONG).show();

            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate( Sign_upDirections.actionSignUpToCongratulation(binding.editName.getText().toString(), getString(R.string.CR)));
        }

    }

}