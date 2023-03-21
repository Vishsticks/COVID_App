package com.covid_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.covid_app.room.AppDatabaseBuilder;
import com.covid_app.room.Student;
import com.covid_app.room.StudentDao;
import com.covid_app.room.StudentRepository;
import com.covid_app.utils.Constants;
import com.covid_app.utils.SharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfileActivity extends AppCompatActivity {
    private EditText edtFullName, edtStudentId, edtEmailAddress, edtCourseName;
    private TextView txtFullName, txtEmailAddress, txtCourseName, txtAlreadySetup;
    private StudentDao studentDao;
    private StudentRepository studentRepository;
    private FirebaseFirestore database;
    private SharedPreference sharedPreference;

    private String TAG = UserProfileActivity.class.getSimpleName();
    private boolean isAlreadySetup = false;

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

        txtFullName = findViewById(R.id.txt_full_name);
        txtEmailAddress = findViewById(R.id.txt_email_address);
        txtCourseName = findViewById(R.id.txt_course_name);

        txtAlreadySetup = findViewById(R.id.txt_already_setup);

        studentDao = AppDatabaseBuilder.getDatabase(UserProfileActivity.this).getStudentDao();
        studentRepository = new StudentRepository(studentDao);

        database = FirebaseFirestore.getInstance();
        sharedPreference = new SharedPreference(UserProfileActivity.this);
    }

    void initialiseListeners() {
        findViewById(R.id.btn_submit_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                if (!isAlreadySetup) {
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
                                        hideProgressBar();
                                        sharedPreference.setStudentId(studentId);
                                        clearData();
                                        Toast.makeText(UserProfileActivity.this, "Record saved.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, ">>>>> Id ::" + e.getLocalizedMessage());
                                        hideProgressBar();
                                        Toast.makeText(UserProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    String studentId = edtStudentId.getText().toString().trim();
                    database.collection(Constants.DbCollection.COLLECTION_USERS)
                            .whereEqualTo(Constants.UserContacts.userId, studentId)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    hideProgressBar();
                                    if (task.isSuccessful()) {
                                        QuerySnapshot result = task.getResult();
                                        sharedPreference.setStudentId(studentId);
                                        edtStudentId.setText("");
                                        if (result.size() > 0) {
                                            finish();
                                            Toast.makeText(UserProfileActivity.this, "Account setup done.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(UserProfileActivity.this, getString(R.string.no_record_found_with_userid), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }

            }
        });

        txtAlreadySetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAlreadySetup) {
                    txtFullName.setVisibility(View.GONE);
                    txtEmailAddress.setVisibility(View.GONE);
                    txtCourseName.setVisibility(View.GONE);

                    edtFullName.setVisibility(View.GONE);
                    edtEmailAddress.setVisibility(View.GONE);
                    edtCourseName.setVisibility(View.GONE);

                    txtAlreadySetup.setText(R.string.str_setup);
                } else {
                    txtFullName.setVisibility(View.VISIBLE);
                    txtEmailAddress.setVisibility(View.VISIBLE);
                    txtCourseName.setVisibility(View.VISIBLE);

                    edtFullName.setVisibility(View.VISIBLE);
                    edtEmailAddress.setVisibility(View.VISIBLE);
                    edtCourseName.setVisibility(View.VISIBLE);

                    txtAlreadySetup.setText(R.string.str_already_setup);
                }
                isAlreadySetup = !isAlreadySetup;
            }
        });
    }

    void clearData() {
        edtStudentId.setText("");
        edtFullName.setText("");
        edtCourseName.setText("");
        edtEmailAddress.setText("");
    }

    private void showProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}