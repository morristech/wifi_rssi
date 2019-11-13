package com.aruba.wifirssi.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aruba.wifirssi.model.WifiAccessPoint;

@Database(entities = {WifiAccessPoint.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public abstract WifiAccessPointDAO accessPointDao();

    @VisibleForTesting
    public static final String DATABASE_NAME = "wifi-rssi-db";

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries() // this isn't best practise but for this assessment i will use this for simplicity
                            .build();
                }
            }
        }
        return sInstance;
    }


}
