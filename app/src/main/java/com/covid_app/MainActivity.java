package com.covid_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.covid_app.utils.Constants;
import com.covid_app.utils.SharedPreference;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    private MessageListener messageListener;
    private Message message;
    private String userId = "";
    private SharedPreference sharedPreference;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
        initListeners();
    }

    private void initialise() {
        sharedPreference = new SharedPreference(MainActivity.this);
        database = FirebaseFirestore.getInstance();

        messageListener = new MessageListener() {
            @Override
            public void onBleSignalChanged(@NonNull Message message, @NonNull BleSignal bleSignal) {
                super.onBleSignalChanged(message, bleSignal);
                Log.e(">>>>>", "onBleSignalChanged");
                Toast.makeText(MainActivity.this, "onBleSignalChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDistanceChanged(@NonNull Message message, @NonNull Distance distance) {
                super.onDistanceChanged(message, distance);
                Log.e(">>>>>", "onDistanceChanged");
                Toast.makeText(MainActivity.this, "onDistanceChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFound(@NonNull Message message) {
                super.onFound(message);
                Log.e(">>>>>", "onFound");
                Toast.makeText(MainActivity.this, "Found::" + message.getContent().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost(@NonNull Message message) {
                super.onLost(message);
                Log.e(">>>>>", "onLost");
                Toast.makeText(MainActivity.this, "onLost callback", Toast.LENGTH_SHORT).show();
            }
        };

        message = new Message(userId.getBytes(StandardCharsets.UTF_8));
    }

    private void initListeners() {

        findViewById(R.id.txt_user_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            }
        });

        findViewById(R.id.txt_guildlines).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CovidGuidlinesActivity.class));
            }
        });

        findViewById(R.id.txt_symptom_checker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SymptomsCheckerActivity.class));
            }
        });

        findViewById(R.id.txt_location_check_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LocationCheckInActivity.class));
            }
        });

        findViewById(R.id.txt_PPE_usage_monitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PPEMonitorUsageActivity.class));
            }
        });

        findViewById(R.id.txt_guildlines).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CovidGuidlinesActivity.class));
            }
        });

        findViewById(R.id.txt_exposure_notifitcation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExposureNotificationActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = sharedPreference.getStudentId();
        getScore();
        Nearby.getMessagesClient(this).subscribe(messageListener);
        Nearby.getMessagesClient(this).publish(message);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
        Nearby.getMessagesClient(this).unpublish(message);
    }

    public void getScore() {
        showScoreProgress();
        database.collection(Constants.DbCollection.COLLECTION_ATTENDANCE)
                .whereEqualTo(Constants.CovidStatus.userId, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        hideScoreProgress();
                        if (task.isSuccessful()) {
                            int result = 0;
                            Log.e(TAG, ">>>>> No of attendence ::" + task.getResult().size());
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                String type = queryDocumentSnapshot.getString(Constants.Attendance.type);
                                switch (type) {
                                    case "sanitizer":
                                        result = result + 5;
                                        break;
                                    case "mask":
                                        result = result + 10;
                                        break;
                                    default:
                                        result = result - 5;
                                }
                            }
                            ((TextView) findViewById(R.id.txt_score_value)).setText(String.valueOf(result));
                        }
                    }
                });
    }

    private void showScoreProgress() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideScoreProgress() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}