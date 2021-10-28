package com.passion.attendancesheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.passion.attendancesheet.fragments.LoginDirections;

import timber.log.Timber;


public class LoginActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        navController = ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.login_nav_host_fragment)).getNavController();

        // AppbarConfiguration
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.fragment_home_id,
//                R.id.adminHome
//        ).build();

        // Add up button in navigation button , where navController is provided to navigate back using up button , but the behaviour is not set yet.
        NavigationUI.setupActionBarWithNavController(this, navController);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.login_nav_host_fragment)).getNavController();

        if( currentUser != null  ){
            currentUser.reload();

            if(currentUser.getEmail().equals("0i0am1a1programmer@gmail.com")) {
                navController.navigate(LoginDirections.actionLoginToAdminActivity(currentUser.getEmail()));
               finish();
            }
            else
                navController.navigate( LoginDirections.actionLoginToNavigation() );
        }
        else{
            Toast.makeText(this, "NO current User", Toast.LENGTH_LONG).show();
        }

        Timber.i("Login Activity onCreate called and user is checked");
    }



    @Override
    public boolean onSupportNavigateUp() {
        // allow up button to navigate back or set the behaviour of up button
        return navController.navigateUp();
    }
}