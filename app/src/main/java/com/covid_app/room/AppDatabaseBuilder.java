package com.covid_app.room;

import android.content.Context;

import androidx.room.Room;

public class AppDatabaseBuilder {
    public static AppDatabase appDatabase = null;

    public synchronized static AppDatabase getDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "CovidApp").build();
        }
        return appDatabase;
    }
}
