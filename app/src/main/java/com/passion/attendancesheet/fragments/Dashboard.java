package com.passion.attendancesheet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentDashboardBinding;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;
import com.passion.attendancesheet.utils.Accessory_tool;

import java.util.Objects;

import timber.log.Timber;

public class Dashboard extends Fragment {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    DashboardAdapter adapter;
    FragmentDashboardBinding binding;
    NavController navController;

    String courseId = null;
    int strength = -1;
    AttendanceSheetViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)(getActivity())).getSupportActionBar().setElevation(0);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentDashboardBinding.inflate(getLayoutInflater());
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get( AttendanceSheetViewModel.class );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // hide tab and view Pager while loading
        if( courseId == null && strength <= 0 )
        binding.tabAndViewPager.setVisibility(View.GONE);

        // Get the current courses
        FirebaseDatabase  db = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db.getReference().child("crs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot courses : snapshot.getChildren() ) {
                    for (DataSnapshot crs : courses.getChildren()) {
                        if (crs.child("email").getValue(String.class).equals(currentUser.getEmail())) {
                            courseId = courses.getKey();
                            courseId = courseId.split(" ")[0] + "-" + Accessory_tool.getIntFromRoman(courseId.split(" ")[1]);

                            Toast.makeText(getContext(), "Course : " + courseId, Toast.LENGTH_LONG).show();

                            // check if sheet import or not based on strength
                            checkIfSheetAvailable(courseId);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void prepareDashboard(){

        View view = binding.getRoot();

        if( courseId != null && strength > 0 ) {

            binding.tabAndViewPager.setVisibility(View.VISIBLE);
            binding.lottieAnimationView.cancelAnimation();
            binding.lottieAnimationView.setVisibility(View.GONE);

            tabLayout = view.findViewById(R.id.tabLayout);
            adapter = new DashboardAdapter(this, courseId, strength);
            viewPager2 = view.findViewById(R.id.viewpager2);
            viewPager2.setAdapter(adapter);

            Timber.d("Sheet Prepared");


            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                if (position == 0) {
                    tab.setText("Home");
                } else {
                    tab.setText("History");
                }
            }).attach();

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.cr_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.action_navigation_to_login){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText( getContext(), "Signed Out", Toast.LENGTH_LONG).show();

            navController.navigate( DashboardDirections.actionNavigationToLogin());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkIfSheetAvailable( String course_name ) {

        Timber.d("check if sheet Available called");

        if( course_name == null || course_name.isEmpty() ){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT ).show();
            return;
        }

        // Check if sheet imported if yes , Navigate to CR Home is sheet available, other wise go to ImportSheet
        viewModel.getStudentCount(course_name).observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer str) {
                if( str == null || str == 0 ){
                    navController.navigate( DashboardDirections.actionDashboardToImportSheet(course_name));
                    Toast.makeText(getContext(), "No Sheet found of " + course_name, Toast.LENGTH_LONG).show();
                    Timber.d("course readied %s", course_name);

                }
                else{

                    // Prepare DashBoard
                    Timber.d("Prepare Dashboard called");
                    strength = str;
                    prepareDashboard();

                }


            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("OnDestroy");
    }
}
