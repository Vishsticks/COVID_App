package com.covid_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.covid_app.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import org.checkerframework.checker.units.qual.A;

import java.util.Scanner;

public class QRCodeScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private String TAG = QRCodeScannerActivity.class.getSimpleName();
    private static String KEY_TYPE = "key_type";
    private static final String QR_PREFIX = "class_";
    private int type = 0;

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scannar);
        type=getIntent().getIntExtra(KEY_TYPE,0);
        initialise();
    }

    private void initialise() {
        CodeScannerView codeScannerView = findViewById(R.id.code_scanner_view);
        mCodeScanner = new CodeScanner(this, codeScannerView);
        database = FirebaseFirestore.getInstance();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                String data = result.getText();
                Log.d(TAG, ">>>>> Result ::" + data);
                if (data.contains(QR_PREFIX)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (type == 0) {
                                Toast.makeText(QRCodeScannerActivity.this, "CheckIn Completed.", Toast.LENGTH_SHORT).show();
                                Attendance attendance=new Attendance();
                                attendance.setUserId("xyz");
                                attendance.setType(0);
                                attendance.setTimestamp(System.currentTimeMillis());
                                database.collection(Constants.DbCollection.COLLECTION_ATTENDANCE)
                                        .add(attendance)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.e(TAG, ">>>>> Id ::" + documentReference.getId());
                                                Toast.makeText(QRCodeScannerActivity.this, "Record saved.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, ">>>>> Id ::" + e.getLocalizedMessage());
                                                Toast.makeText(QRCodeScannerActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }


                            else
                                Toast.makeText(QRCodeScannerActivity.this, "CheckOut Completed.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(QRCodeScannerActivity.this, "Invalid class.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.stopPreview();
    }

    public static Intent getIntent(Context context, int type) {
        Intent intent = new Intent(context, QRCodeScannerActivity.class);
        intent.putExtra(KEY_TYPE, type);
        return intent;
    }
}

//https://github.com/yuriy-budiyev/code-scanner