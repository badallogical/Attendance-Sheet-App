package com.passion.attendancesheet;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.passion.attendancesheet.adapters.PageAdapter;
import com.passion.attendancesheet.room.SheetViewModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 2;

    ViewPager viewPager;
    PageAdapter pagerAdapter;
    SheetViewModel viewModel;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            //  TODO : import sheet.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
                // Ask for permission check
                if( checkPermission() ){
                    importSheet();
                }
                else{
                    requestForPermission();
                }
            }
            else{
                // Go ahead
                importSheet();
            }

        });

        // ViewModel
        viewModel = ViewModelProviders.of(this).get(SheetViewModel.class);

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), viewModel);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This Permission is needed to read file from your storage.")
                    .setPositiveButton("ok", (dialog, which) -> {
                        // Request Permission
                        ActivityCompat.requestPermissions( MainActivity.this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
                    })
                    .setNegativeButton("cancel", (dialog, which ) -> {
                        dialog.dismiss();
                    }).create().show();
        }
        else{
            // Request Permission
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if( requestCode == READ_REQUEST_CODE ){
            if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                // Show popup and perform operation safely.
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                importSheet();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void importSheet() {
        Intent filePickerIntent = new Intent( Intent.ACTION_OPEN_DOCUMENT );
        filePickerIntent.addCategory( Intent.CATEGORY_OPENABLE );
        filePickerIntent.setType("application/vnd.ms-excel");
        startActivityForResult( filePickerIntent, READ_REQUEST_CODE );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if ( requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK ) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    assert uri != null;
                    File xl_file = new File(Environment.getExternalStorageDirectory() + "/" + Objects.requireNonNull(uri.getPath()).split(":")[1]);
                    FileInputStream myInput = new FileInputStream(xl_file);

                    // Create a POIFSFileSystem object 
                    POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

                    // Create a workbook using the File System 
                    HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

                    // Get the first sheet from workbook 
                    HSSFSheet mySheet = myWorkBook.getSheetAt(0);

                    Iterator rowIter = mySheet.rowIterator();

                    while (rowIter.hasNext()) {
                        HSSFRow myRow = (HSSFRow) rowIter.next();
                        Iterator cellIter = myRow.cellIterator();
                        while (cellIter.hasNext()) {
                            HSSFCell myCell = (HSSFCell) cellIter.next();
                            Log.d("XL file", "Cell Value: " + myCell.toString());
                            Toast.makeText(this, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
