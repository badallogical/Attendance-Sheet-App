package com.passion.attendancesheet.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodayListAdapter extends RecyclerView.Adapter<TodayListAdapter.TodayListViewHolder> {


    @NonNull
    @Override
    public TodayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TodayListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TodayListViewHolder extends RecyclerView.ViewHolder{
            TextView lecture;
            TextView teacher;
            TextView present;
            TextView absents;
            TextView bunks;

        public TodayListViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
