package com.covid_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private SharedPreferences mSharedPreferences;
    private static String KEY_STUDENT_ID = "student_id";

    public SharedPreference(Context mContext) {
        mSharedPreferences = mContext.getApplicationContext().getSharedPreferences("CovidAppPref", Context.MODE_PRIVATE);
    }

    public void setStudentId(String id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_STUDENT_ID, id);
        editor.commit();
    }

    public String getStudentId() {
        return mSharedPreferences.getString(KEY_STUDENT_ID, "1234");
    }

}
