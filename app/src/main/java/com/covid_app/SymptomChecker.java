package com.covid_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BulletSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SymptomChecker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker);
    }
}


//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        LinearLayout bulletPointsContainer = findViewById(R.id.covid_points_container);
//        String[] bulletPoints = getResources().getStringArray(R.array.covid_points);
//
//        for (String item : bulletPoints) {
//            TextView textView = new TextView(this);
//            textView.setText(getBulletPointSpannable(item));
//            bulletPointsContainer.addView(textView);
//        }
//    }
//
//    private Spannable getBulletPointSpannable(String text) {
//        Spannable spannable = new SpannableString(text);
//        spannable.setSpan(new BulletSpan(16), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        return spannable;
//    }
//}