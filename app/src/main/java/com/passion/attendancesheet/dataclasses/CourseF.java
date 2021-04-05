package com.passion.attendancesheet.dataclasses;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseF implements Serializable {

    private String name;
    private int strength;
    private ArrayList<ClassRepresentative> crs;
    private ArrayList<StudentF> students;
    private ArrayList<String> teachers;

    public CourseF(){}

    public CourseF(String name, int strength, ArrayList<ClassRepresentative> crs, ArrayList<StudentF> students, ArrayList<String> teachers ){
        this.name = name;
        this.strength = strength;
        this.crs = crs;
        this.students = students;
        this.teachers = teachers;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public ArrayList getCrs() {
        return crs;
    }

    public ArrayList getStudents() {
        return students;
    }

    public ArrayList getTeachers() {
        return teachers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setCrs(ArrayList crs) {
        this.crs = crs;
    }

    public void setStudents(ArrayList students) {
        this.students = students;
    }

    public void setTeachers(ArrayList teachers) {
        this.teachers = teachers;
    }
}
