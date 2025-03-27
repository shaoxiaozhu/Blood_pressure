package com.example.bpmonitor.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bpmonitor.models.UserInfo;

@Database(entities = {UserInfo.class, Measurement.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    
    public abstract UserInfoDao userInfoDao();
    public abstract MeasurementDao measurementDao();
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "bpmonitor.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}