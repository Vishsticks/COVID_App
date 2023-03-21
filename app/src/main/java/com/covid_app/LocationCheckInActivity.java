package com.covid_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class LocationCheckInActivity extends AppCompatActivity {
    private TextView txtCheckIn;
    private TextView txtCheckOut;
    private int currentType=0;

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_check_in);
        initialise();
        initialiseListeners();
    }

    private void initialise() {
        txtCheckIn = findViewById(R.id.txt_check_in);
        txtCheckOut = findViewById(R.id.txt_check_out);
        database = FirebaseFirestore.getInstance();
    }

    private void initialiseListeners() {
        txtCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentType=0;
                if (ContextCompat.checkSelfPermission(LocationCheckInActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    startActivity(QRCodeScannerActivity.getIntent(LocationCheckInActivity.this, 0));
                else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},200);
                }
            }
        });
        txtCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentType=1;
                if (ContextCompat.checkSelfPermission(LocationCheckInActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    startActivity(QRCodeScannerActivity.getIntent(LocationCheckInActivity.this, 1));
                else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},200);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200 && grantResults.length>0){
            startActivity(QRCodeScannerActivity.getIntent(LocationCheckInActivity.this, currentType));
        }
    }
}

