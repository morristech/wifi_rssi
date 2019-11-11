package com.aruba.wifirssi;

import android.app.Application;

import com.aruba.wifirssi.data.AppDatabase;

public class WifiApp extends Application {

    private static WifiApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getApp() {
        return instance;
    }

    public static AppDatabase getDatabase() {
        return AppDatabase.getInstance(getApp());
    }

}
