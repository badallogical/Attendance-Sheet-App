package com.passion.attendancesheet.room.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class SheetWithAttendace {

    @Embedded
    public Sheet sheet;

    @Relation( parentColumn = "sheet_id", entityColumn = "student_id", associateBy = @Junction( Attendance.class ) )
    public List<Student> studentsPresent;

}
