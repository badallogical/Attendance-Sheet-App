package com.passion.attendancesheet.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.adapters.HistoryListAdapter;
import com.passion.attendancesheet.room.SheetViewModel;
import com.passion.attendancesheet.room.entity.Sheet;
import com.passion.attendancesheet.room.view.SheetDetailView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryList extends Fragment {

    RecyclerView history_list;
    Context context;
    SheetViewModel sheetViewModel;
    HistoryListAdapter historyListAdapter;

    LinearLayout no_history;

    public HistoryList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        sheetViewModel = ViewModelProviders.of(this).get(SheetViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // empty view
        no_history = view.findViewById(R.id.no_history);

        history_list = view.findViewById(R.id.history_list);
        historyListAdapter = new HistoryListAdapter(context );
        history_list.setAdapter(historyListAdapter);
        history_list.setLayoutManager(new LinearLayoutManager(context));
        sheetViewModel.getAllSheets().observe(this, new Observer<List<SheetDetailView>>() {
            @Override
            public void onChanged(List<SheetDetailView> sheetDetailViews) {
                historyListAdapter.setHistories( sheetDetailViews );
                if( sheetDetailViews.isEmpty() ){
                    no_history.setVisibility(View.VISIBLE);
                    history_list.setVisibility( View.GONE );
                }
                else{
                    no_history.setVisibility( View.GONE );
                    history_list.setVisibility( View.VISIBLE );
                }
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
                sheetViewModel.deleteSheetById(sheet_id);
                sheetViewModel.deleteSheetAttendance(sheet_id);
            }
        }).attachToRecyclerView(history_list);

    }
}
