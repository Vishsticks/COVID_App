package com.covid_app.room;

import java.util.List;

public class StudentRepository {
    StudentDao studentDao;

    public StudentRepository(StudentDao dao) {
        this.studentDao = dao;
    }

    public long insertStudent(Student student) {
        Student stu = studentDao.getStudent(student.id);
        if (stu == null)
            return studentDao.insert(student);
        else
            return -1;
    }

    public List<Student> getAllStudents() {
        return studentDao.getAllStudent();
    }

    Student getStudent(String id) {
        return studentDao.getStudent(id);
    }
}
