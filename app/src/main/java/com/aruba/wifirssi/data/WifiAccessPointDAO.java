package com.aruba.wifirssi.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aruba.wifirssi.model.WifiAccessPoint;

import java.util.List;

@Dao
public interface WifiAccessPointDAO {
    @Query("SELECT * FROM wifi_access_points")
    List<WifiAccessPoint> getAll();

    @Query("SELECT * FROM wifi_access_points WHERE id IN (:ids)")
    List<WifiAccessPoint> loadAllByIds(int[] ids);

    @Query("SELECT * FROM wifi_access_points WHERE wifi_ssid LIKE :ssid LIMIT 1")
    WifiAccessPoint findByName(String ssid);

    @Insert
    void insertAll(WifiAccessPoint... accessPoints);

    @Delete
    void delete(WifiAccessPoint accessPint);
}
