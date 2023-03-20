package com.covid_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.covid_app.room.AppDatabaseBuilder;
import com.covid_app.room.Student;
import com.covid_app.room.StudentDao;
import com.covid_app.room.StudentRepository;
import com.covid_app.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {
    private EditText edtFullName, edtStudentId, edtEmailAddress, edtCourseName;
    private StudentDao studentDao;
    private StudentRepository studentRepository;
    private FirebaseFirestore database;

    private String TAG = UserProfileActivity.class.getSimpleName();

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

        database = FirebaseFirestore.getInstance();
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

                    database.collection(Constants.DbCollection.COLLECTION_USERS)
                            .add(student)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.e(TAG, ">>>>> Id ::" + documentReference.getId());
                                    clearData();
                                    Toast.makeText(UserProfileActivity.this, "Record saved.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, ">>>>> Id ::" + e.getLocalizedMessage());
                                    Toast.makeText(UserProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
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