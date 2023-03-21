package com.covid_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.covid_app.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExposureNotificationActivity extends AppCompatActivity {
    private FirebaseFirestore database;
    private String userId = "1234";
    private String TAG = ExposureNotificationActivity.class.getSimpleName();
    private TextView txtUserCount, txtMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exposure_notification);
        initialise();
    }

    private void initialise() {
        txtUserCount = findViewById(R.id.txt_user_count);
        txtMsg = findViewById(R.id.txt_msg);
        database = FirebaseFirestore.getInstance();
        getRemoteData();
    }

    private void getRemoteData() {
        showProgressBar();
        database.collection(Constants.DbCollection.COLLECTION_USER_CONTACTS)
                .whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        hideProgressBar();
                        if (task.isSuccessful()) {
                            int deviceContacts = task.getResult().size();
                            if (deviceContacts > 1)
                                txtUserCount.setText(deviceContacts + " Users");
                            else
                                txtUserCount.setText(deviceContacts + " User");
                            Log.e(TAG, "deviceContacts Size ::" + deviceContacts);
                            List<String> userIds = new ArrayList();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String contactUserId = documentSnapshot.get(Constants.UserContacts.contactUserId).toString();
                                Log.e(TAG, ">>>>> Id ::" + documentSnapshot.getId());
                                Log.e(TAG, ">>>>> Contact User Id ::" + contactUserId);
                                Log.e(TAG, ">>>>> timestamp ::" + documentSnapshot.get(Constants.UserContacts.timeStamp));
                                userIds.add(contactUserId);
                            }
                            checkUserInfectedOrNot(userIds);
                        } else {
                            // Error
                            Toast.makeText(ExposureNotificationActivity.this, "Error fetching records.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUserInfectedOrNot(List<String> contacts) {
        database.collection(Constants.DbCollection.COLLECTION_COVID_STATUS)
                .whereEqualTo(Constants.CovidStatus.covidStatus, "positive")
                .whereIn(Constants.CovidStatus.userId, contacts)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot.size() == 0) {
                                Log.e(TAG, "No contact with infected person.");
                                txtMsg.setText(getString(R.string.no_contact_with_infected_person));
                                txtMsg.setTextColor(ContextCompat.getColor(ExposureNotificationActivity.this, R.color.color_green));
                            } else {
                                Log.e(TAG, "Contact with infected person.");
                                txtMsg.setText(getString(R.string.contact_with_infected_person));
                                txtMsg.setTextColor(ContextCompat.getColor(ExposureNotificationActivity.this, R.color.color_red));
                            }
                        } else {
                            // Error
                            Toast.makeText(ExposureNotificationActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

}