package com.passion.attendancesheet.dataclasses;

import java.io.Serializable;

public class ClassRepresentative implements Serializable {

    private String name;
    private String email;

    public ClassRepresentative(){}

    public ClassRepresentative( String name, String email ){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
