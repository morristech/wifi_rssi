package com.aruba.wifirssi.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.squareup.moshi.Json;

@Entity(tableName = "wifi_access_points")
public class WifiAccessPoint implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Json(name = "id")
    private int id;
    @ColumnInfo(name = "wifi_ssid")
    @Json(name = "ssid")
    private String ssid;
    @ColumnInfo(name = "rssi_value")
    @Json(name = "rssi")
    private double rssi;

    public final static Parcelable.Creator<WifiAccessPoint> CREATOR = new Creator<WifiAccessPoint>() {
        @Override
        public WifiAccessPoint createFromParcel(Parcel in) {
            return new WifiAccessPoint(in);
        }

        @Override
        public WifiAccessPoint[] newArray(int size) {
            return (new WifiAccessPoint[size]);
        }
    };

    protected WifiAccessPoint(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.ssid = ((String) in.readValue((String.class.getClassLoader())));
        this.rssi = ((double) in.readValue((double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    @Ignore
    public WifiAccessPoint() {
    }

    /**
     * @param id
     * @param ssid
     * @param rssi
     */
    public WifiAccessPoint(int id, String ssid, double rssi) {
        super();
        this.id = id;
        this.ssid = ssid;
        this.rssi = rssi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public double getRssi() {
        return rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(ssid);
        dest.writeValue(rssi);
    }

    public int describeContents() {
        return 0;
    }

}