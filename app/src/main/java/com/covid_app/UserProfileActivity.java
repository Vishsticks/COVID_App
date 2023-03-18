package com.covid_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.covid_app.room.AppDatabase;
import com.covid_app.room.AppDatabaseBuilder;
import com.covid_app.room.Student;
import com.covid_app.room.StudentDao;
import com.covid_app.room.StudentRepository;

public class UserProfileActivity extends AppCompatActivity {
    private EditText edtFullName, edtStudentId, edtEmailAddress, edtCourseName;
    private StudentDao studentDao;
    private StudentRepository studentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initialise();
        initialiseListeners();
    }

    void initialise() {
        edtFullName = findViewById(R.id.edt_full_name);
        edtStudentId = findViewById(R.id.edt_student_id);
        edtEmailAddress = findViewById(R.id.edt_email_address);
        edtCourseName = findViewById(R.id.edt_course_name);

        studentDao = AppDatabaseBuilder.getDatabase(UserProfileActivity.this).getStudentDao();
        studentRepository = new StudentRepository(studentDao);
    }

    void initialiseListeners() {
        findViewById(R.id.btn_submit_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = edtFullName.getText().toString().trim();
                String studentId = edtStudentId.getText().toString().trim();
                String emailAddress = edtEmailAddress.getText().toString().trim();
                String courseName = edtCourseName.getText().toString().trim();

                if (fullName.isEmpty() || studentId.isEmpty() || emailAddress.isEmpty() || courseName.isEmpty()) {
                    Toast.makeText(UserProfileActivity.this, "Please fill details.", Toast.LENGTH_SHORT).show();
                } else {
                    Student student = new Student(studentId, fullName, emailAddress, courseName);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long insertedId = studentRepository.insertStudent(student);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (insertedId != -1) {
                                        Toast.makeText(UserProfileActivity.this, "Record saved.", Toast.LENGTH_SHORT).show();
                                        clearData();
                                    }else{
                                        Toast.makeText(UserProfileActivity.this, "Student Id already exist.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    void clearData() {
        edtStudentId.setText("");
        edtFullName.setText("");
        edtCourseName.setText("");
        edtEmailAddress.setText("");
    }
}