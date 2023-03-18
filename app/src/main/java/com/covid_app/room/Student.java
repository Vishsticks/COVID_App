package com.covid_app.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    @PrimaryKey @NonNull
    String id;
    String fullName;
    String emailAddress;
    String courseName;

    public Student() {

    }

    public Student(String id, String fullName, String emailAddress, String courseName) {
        this.id = id;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.courseName = courseName;
    }

    @NonNull
    @Override
    public String toString() {
        return ">>Student Id ::" + id + ">>Name ::" + fullName + ">>Email Address ::" + emailAddress + ">>Course Name::" + courseName;
    }
}
