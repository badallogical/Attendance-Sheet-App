package com.passion.attendancesheet.fragments.admin;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.AdminCourseListAdapter;
import com.passion.attendancesheet.adapters.CourseListClick;
import com.passion.attendancesheet.databinding.FragmentAdminHomeBinding;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AdminHome extends Fragment implements CourseListClick {

    FragmentAdminHomeBinding binding;
    List<String> array;
    AdminCourseListAdapter adapter;

    public AdminHome() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("Admin Home Created");

        binding = FragmentAdminHomeBinding.inflate(getLayoutInflater());

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        assert currentUser != null;
        if( currentUser.isEmailVerified() ){

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference();

            array = new ArrayList<String>();
            adapter = new AdminCourseListAdapter( array , getContext() , this );

            // TODO : update change in course data
            /* Child event listener is used to perform action on the event happen to the child just add that listener to the parent refrence
            * This listener not only called when the data changed but at the start of the app , so its perfect, better then onDataChanged listener as it returen the whole data set.
            * */
            ref.child("courses").orderByKey().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Timber.d("add child listener called");
                    array.add( snapshot.getValue(String.class));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Timber.d("remove child event called");
                    array.remove( snapshot.getValue(String.class));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // Fetch course data from firebase
            //fetchCourse(ref);

            // setup course list
            binding.courseList.setAdapter(adapter);
            binding.courseList.setLayoutManager( new LinearLayoutManager(getContext()));

            // setup course list swipe listener
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    // removeValue is used to delete just get the reference to the node you want to delete ( remember equalTo is used to check equality in firebase rather then equal
                    ref.child("courses").orderByValue().equalTo( adapter.getItem(viewHolder.getAdapterPosition() ) ).limitToFirst(1).
                }
            }).attachToRecyclerView(binding.courseList);
        }
        else{
            Toast.makeText(getContext(), "Email is not verified", Toast.LENGTH_LONG).show();
        }

        // Add course to firebase
        binding.addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                        View custom_layout = LayoutInflater.from(getContext()).inflate(R.layout.add_course_dialog, null );

                        // create and show dialog
                        dialogBuilder.setView( custom_layout )
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO:
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = db.getReference("courses");

                                        EditText editCourse = (EditText)custom_layout.findViewById(R.id.course_name);
                                        if( editCourse.getText().toString().trim().isEmpty() ){
                                            editCourse.setError("Empty Field");
                                        }
                                        else{
                                            // adding child to the course ref, ( its a way of creating list , on the way remember )
                                            if( array.contains(editCourse.getText().toString().toUpperCase())){
                                                Toast.makeText(getContext(), "Course Already Existed", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                ref.push().setValue(editCourse.getText().toString().toUpperCase());
                                            }
                                        }

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("Add Course")
                                .create().show();

                    }
                });

    }

    private void fetchCourse(DatabaseReference ref) {
        ref.child("courses").orderByKey().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if( task.isSuccessful() ){
                    DataSnapshot snapshot = task.getResult();
                    for( DataSnapshot dataSnapshot : snapshot.getChildren()){
                        array.add( dataSnapshot.getValue(String.class));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void openCrPanel(String course ) {
        NavHostFragment.findNavController(this).navigate( AdminHomeDirections.actionAdminHomeToCourseCR( course ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("Admin Home Destroyed");
    }
}