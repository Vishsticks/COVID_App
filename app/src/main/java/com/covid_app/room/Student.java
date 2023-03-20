package com.covid_app.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    @PrimaryKey @NonNull
    String id;
    String name;
    String email;
    String courseName;

    public Student() {

    }

    public Student(String id, String name, String emailAddress, String courseName) {
        this.id = id;
        this.name = name;
        this.email = emailAddress;
        this.courseName = courseName;
    }

    @NonNull
    @Override
    public String toString() {
        return ">>Student Id ::" + id + ">>Name ::" + name + ">>Email Address ::" + email + ">>Course Name::" + courseName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
