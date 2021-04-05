package com.passion.attendancesheet.dataclasses;

import java.io.Serializable;

public class ClassRepresentative implements Serializable {

    private String name;
    private String email;
    private String course;

    public ClassRepresentative(){}

    public ClassRepresentative( String name, String email, String course ){
        this.name = name;
        this.course = course;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
