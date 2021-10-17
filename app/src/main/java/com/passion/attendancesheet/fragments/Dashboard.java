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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.databinding.FragmentDashboardBinding;

import java.util.Objects;

public class Dashboard extends Fragment {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    DashboardAdapter adapter;
    FragmentDashboardBinding binding;
    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentDashboardBinding.inflate(getLayoutInflater());
        navController = NavHostFragment.findNavController(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity)(getActivity())).getSupportActionBar().setElevation(0);

        tabLayout = view.findViewById(R.id.tabLayout);
        adapter = new DashboardAdapter(this);
        viewPager2 = view.findViewById(R.id.viewpager2);
        viewPager2.setAdapter( adapter );

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                     if( position == 0 ){
                         tab.setText("Home");
                     }
                     else{
                         tab.setText("History");
                     }
            }
        }).attach();
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
}
