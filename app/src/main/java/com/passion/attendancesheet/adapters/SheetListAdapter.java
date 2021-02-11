package com.passion.attendancesheet.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.passion.attendancesheet.AttendanceActivity;
import com.passion.attendancesheet.MainActivity;
import com.passion.attendancesheet.R;
import com.passion.attendancesheet.fragments.SheetList;
import com.passion.attendancesheet.room.SheetViewModel;
import com.passion.attendancesheet.room.entity.Course;
import com.passion.attendancesheet.room.view.CourseTeacherView;
import com.passion.attendancesheet.utils.Accessory_tool;

import java.util.ArrayList;
import java.util.List;

public class SheetListAdapter extends RecyclerView.Adapter<SheetListAdapter.SheetListViewHolder> {

    Context context;

    static List<Course> courseList = new ArrayList<>();
    static SheetList sheetList;

    public SheetListAdapter(Context context, SheetList sheetListref){
        this.context = context;
        sheetList = sheetListref;
    }


    @Override
    public SheetListViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sheet_list_item, parent, false );

        return new SheetListViewHolder(v, parent, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetListViewHolder holder, int position) {
        if (courseList.size() > 0) {
            Course course = courseList.get(position);
            String mCourseName = course.course_id.split("-")[0] + " " + Accessory_tool.getRomanFromInt( Integer.parseInt(course.course_id.split("-")[1]) );
            holder.courseName.setText(mCourseName);
            holder.courseName.setTag(position);
            holder.course_id = course.course_id;
        }
    }


    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void setCourseList( List<Course> courseList ){
        SheetListAdapter.courseList =  courseList;
        notifyDataSetChanged();
    }


    static class SheetListViewHolder extends RecyclerView.ViewHolder{

        TextView courseName;
        String course_id;
        static AlertDialog dialog;


        SheetListViewHolder(@NonNull View itemView, ViewGroup parent, Context context) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AttendanceActivity.class);

                        // get the prepare sheet layout
                        View dailogview = LayoutInflater.from(context).inflate(R.layout.prepare_sheet_dialog, parent, false);

                        // get missing subject alert textview
                        TextView missing_subject_alert = dailogview.findViewById(R.id.missing_subject);

                        // create alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(dailogview);
                        builder.setTitle("Prepare Your Sheet");

                        SheetViewModel sheetViewModel = ViewModelProviders.of(sheetList).get(SheetViewModel.class);

                        Spinner teacher_spinner = dailogview.findViewById(R.id.teacher_spinner);
                        TeacherListAdapter teacherListAdapter = new TeacherListAdapter(context);
                        teacher_spinner.setAdapter( teacherListAdapter );

                        sheetViewModel.getTeacherCourse(course_id).observe(sheetList, courseWithTeachers -> {
                            teacherListAdapter.setTeachers(courseWithTeachers);
                        });


                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // check for subject field ( validate )
                                EditText subject = dailogview.findViewById(R.id.edit_subject);
                                if( subject.getText().toString().trim().isEmpty() ){
                                   missing_subject_alert.setVisibility(View.VISIBLE);
                                }
                                else {
                                    missing_subject_alert.setVisibility(View.GONE);


                                    intent.putExtra(AttendanceActivity.MODE, context.getResources().getString(R.string.normal));
                                    intent.putExtra(AttendanceActivity.COURSE, courseList.get(Integer.parseInt(courseName.getTag().toString())));
                                    CourseTeacherView curTeacher = (CourseTeacherView) teacher_spinner.getSelectedView().getTag();
                                    intent.putExtra(AttendanceActivity.TEACHER, curTeacher);

                                    intent.putExtra(AttendanceActivity.LECTURE, ((Spinner) dailogview.findViewById(R.id.lecture_spinner)).getSelectedItem().toString());
                                    intent.putExtra(AttendanceActivity.SUBJECT, subject.getText().toString());

                                    context.startActivity(intent);
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);


                    // show dialog
                    dialog.show();

                }
            });


            // OnLongClick delete option
            itemView.setOnLongClickListener( new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {

                    // Save Current Normal Card Color
                    Accessory_tool.defaultCardColor = v.getBackground();

                    // Highlight the selected course( card view )
                    v.setBackgroundResource(R.color.longClick);

                    MainActivity mainActivity = Accessory_tool.mainActivity;

                    // Invalidate the action bar to show delete option
                    if( mainActivity != null ){
                        // TODO: (Issue) in switch dark mode to normal (vice-versa ) mainactivity is destroyed and recreated but sheet list is have the previous context so it will not work if switching the mode.
                        mainActivity.setACTION_BAR_MODE(1);
                        mainActivity.setSelectedCourseId( course_id );
                        mainActivity.setSelectedCourseView(v);
                        mainActivity.invalidateOptionsMenu();
                    }

                    return true;
                }
            });
        }
    }

}
