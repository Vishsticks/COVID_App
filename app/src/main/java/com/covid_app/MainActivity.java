package com.covid_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private MessageListener messageListener;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
        initListeners();
    }

    private void initialise() {

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

        message = new Message("Hello".getBytes(StandardCharsets.UTF_8));
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
        Nearby.getMessagesClient(this).subscribe(messageListener);
        Nearby.getMessagesClient(this).publish(message);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
        Nearby.getMessagesClient(this).unpublish(message);
    }
}