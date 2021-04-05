package com.passion.attendancesheet.dataclasses;

import java.io.Serializable;

public class StudentF implements Serializable {

    private String name;
    private int roll;

    public StudentF(String name, int roll ){
        this.name = name;
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public int getRoll() {
        return roll;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }
}
