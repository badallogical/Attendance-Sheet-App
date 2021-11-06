package com.passion.attendancesheet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.passion.attendancesheet.R;
import com.passion.attendancesheet.model.entity.Attendance_sheet;

import java.util.List;

public class TodayListAdapter extends RecyclerView.Adapter<TodayListAdapter.TodayListViewHolder> {


    Context context;
    List<Attendance_sheet> todaySheet;
    int courseTotalStrength;

    public TodayListAdapter( Context context , int courseTotalStrength ){
        this.context = context;
        this.courseTotalStrength = courseTotalStrength;
    }

    @NonNull
    @Override
    public TodayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.today_list_item, parent, false );
        return new TodayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayListViewHolder holder, int position) {
        Attendance_sheet sheet = todaySheet.get( position );

        holder.lecture.setText( "Lecture " + String.valueOf( sheet.lecture ) );
        holder.subject.setText( sheet.subject );
        holder.teacher.setText( "By " + sheet.teacher.name );
        holder.present.setText( String.valueOf(sheet.presents) );
        holder.absents.setText( String.valueOf(sheet.absents) );

    }

    @Override
    public int getItemCount() {
        if( todaySheet != null )
            return todaySheet.size();
        else
            return 0;
    }

    public void setTodaySheet( List<Attendance_sheet> todaySheet ){
        this.todaySheet = todaySheet;
        notifyDataSetChanged();
    }

    class TodayListViewHolder extends RecyclerView.ViewHolder{
            TextView lecture;
            TextView subject;
            TextView teacher;
            TextView present;
            TextView absents;
            TextView bunks;

        public TodayListViewHolder(@NonNull View itemView) {
            super(itemView);

            lecture = itemView.findViewById(R.id.today_lecture_no);
            subject = itemView.findViewById(R.id.att_subject);
            teacher = itemView.findViewById(R.id.today_teacher_name);
            present = itemView.findViewById(R.id.presnet_score);
            absents = itemView.findViewById(R.id.absent_score);

        }
    }
}
