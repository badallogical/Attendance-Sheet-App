package com.passion.attendancesheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.passion.attendancesheet.fragments.LoginDirections;


public class LoginActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if( currentUser != null  ){
            navController.navigate( LoginDirections.actionLoginToAdminActivity( currentUser.getEmail().toString() ));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        navController = ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.login_nav_host_fragment)).getNavController();

        // add up button in navigation button , where navController is provided to navigate back using up button , but the behaviour is not set yet.
        NavigationUI.setupActionBarWithNavController(this, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        // allow up button to navigate back or set the behaviour of up button
        return navController.navigateUp();
    }
}