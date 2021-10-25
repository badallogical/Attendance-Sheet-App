package com.passion.attendancesheet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.HistoryListAdapter;
import com.passion.attendancesheet.databinding.FragmentHistoryBinding;
import com.passion.attendancesheet.model.AttendanceSheetViewModel;
import com.passion.attendancesheet.model.entity.Attendance_sheet;
import com.passion.attendancesheet.utils.Accessory_tool;

import timber.log.Timber;


/**
 * Display the attendance sheets take so far
 */
public class History extends Fragment implements HistoryListAdapter.HistoryItemClick {


    FragmentHistoryBinding binding;
    AttendanceSheetViewModel viewModel;

    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        navController = NavHostFragment.findNavController(this);

        binding = FragmentHistoryBinding.inflate( getLayoutInflater() );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // Find the current course for a respective CR from firebase
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

            // data persistence

        currentUser.reload();
        db.getReference().child("crs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String courseId = null;
                for( DataSnapshot courses : snapshot.getChildren() ){
                    for( DataSnapshot crs : courses.getChildren() ){
                        if(crs.child("email").getValue(String.class).equals(currentUser.getEmail())){
                            courseId = courses.getKey();
                            courseId = courseId.split(" ")[0] + "-" + Accessory_tool.getIntFromRoman(courseId.split(" ")[1]);

                            Timber.d("Course ID readed : " + courseId);
                            break;
                        }
                    }

                    if( courseId != null ){
                        break;
                    }
                }

                viewModel = new ViewModelProvider(History.this).get( AttendanceSheetViewModel.class );
                HistoryListAdapter adapter = new HistoryListAdapter( getContext(), History.this );
                binding.historyList.setLayoutManager( new LinearLayoutManager(getContext()));
                binding.historyList.setAdapter( adapter );

                viewModel.getAllSheetsByCourseId(courseId).observe( getViewLifecycleOwner(), sheets -> {
                    // setup adapter
                    if( sheets.size() > 0 ){
                        binding.noHistory.setVisibility(View.GONE);
                        binding.historyList.setVisibility(View.VISIBLE);

                        adapter.setHistories( sheets );
                    }
                    else{
                        binding.noHistory.setVisibility(View.VISIBLE);
                        binding.historyList.setVisibility(View.GONE);
                    }

                });

                // on swipe delete history
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT ){
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int sheet_id = ((HistoryListAdapter.HistoryListViewHolder) viewHolder ).getSheet_id();
                        viewModel.deleteSheetById(sheet_id);

                    }
                }).attachToRecyclerView(binding.historyList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void openEditAttendance(Attendance_sheet sheet, String mode) {
        viewModel.getTeacherNameById( sheet.teacher_id ).observe( getViewLifecycleOwner(), teacher_name -> {
            navController.navigate(DashboardDirections.actionDashboardToAttendance(sheet.course_id, Accessory_tool.getRomanFromInt(sheet.lecture), sheet.subject, sheet.teacher_id + "," + teacher_name, mode));
        });
       }
}
