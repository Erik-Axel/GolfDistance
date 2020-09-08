package com.example.navigationmap.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

import com.example.navigationmap.room.dao.ClubDao;
import com.example.navigationmap.room.dao.LocationDataDao;

@Database(entities = {Club.class, LocationData.class}, exportSchema = false, version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static final String DATABASE = "golf_database;";
    private static RoomDatabase instance;

    public static synchronized RoomDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomDatabase.class, DATABASE)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ClubDao clubDao();

    public abstract LocationDataDao locationDataDao();
}
