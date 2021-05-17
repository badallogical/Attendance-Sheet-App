package com.passion.attendancesheet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class AdminActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        navController = ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.admin_fragment_container)).getNavController();

        // support for up button ( not behaviour )
        NavigationUI.setupActionBarWithNavController( this, navController );


        // get the safe args form
        AdminActivityArgs args = AdminActivityArgs.fromBundle( getIntent().getExtras());
        Toast.makeText( this, "Signed in as : " + args.getEmail() , Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId() ){
            case R.id.action_adminHome_to_loginActivity: FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Signed Out", Toast.LENGTH_LONG).show();
            return NavigationUI.onNavDestinationSelected( item, navController );
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        if( mAuth.getCurrentUser() == null ){
//            finish();
//        }
    }
}