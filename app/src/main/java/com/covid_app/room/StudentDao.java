package com.covid_app.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    long insert(Student student);
    @Delete
    void delete(Student student);
    @Update
    void update(Student student);
    @Query("select * from Student where id=:id")
    Student getStudent(String id);
    @Query("select * from Student")
    List<Student> getAllStudent();

}
