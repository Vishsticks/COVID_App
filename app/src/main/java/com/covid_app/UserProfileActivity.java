package com.covid_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        findViewById(R.id.btn_submit_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtFullName =findViewById(R.id.edt_full_name);
                String fullName=edtFullName.getText().toString();
                Log.d("Name",fullName);
                edtFullName.setText(" ");

                EditText edtStudentNumber = findViewById(R.id.edt_student_id);
                String StudentNumber=edtStudentNumber.getText().toString();
                Log.d("Student Number",StudentNumber);
                edtStudentNumber.setText(" ");

                EditText edtEmail = findViewById(R.id.edt_email_address);
                String Email=edtEmail.getText().toString();
                Log.d("Student Number",Email);
                edtEmail.setText(" ");

                EditText edtCourseName = findViewById(R.id.edt_course_name);
                String CourseName = edtCourseName.getText().toString();
                Log.d("Student Course", CourseName);
                edtCourseName.setText(" ");
            }
        });

    }
}