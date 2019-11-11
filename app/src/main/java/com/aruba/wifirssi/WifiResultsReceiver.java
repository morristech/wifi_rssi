package com.aruba.wifirssi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aruba.wifirssi.data.api.WifiRemoteDataSource;
import com.aruba.wifirssi.model.WifiAccessPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WifiResultsReceiver extends BroadcastReceiver {

    private MainActivity miner;

    public WifiResultsReceiver(@NonNull MainActivity miner) {
        super();
        this.miner = miner;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        HashMap<String, ArrayList> rssiList = new HashMap<String, ArrayList>();
        ArrayList<Float> rssi = new ArrayList<Float>();
        if (miner != null && miner.wifiManager != null) {
            List<ScanResult> wifiscan = miner.wifiManager.getScanResults();
            for (ScanResult scanning : wifiscan) {
                final String accessPointName = scanning.SSID;
                final Float rssiValue = (float) scanning.level;
                WifiAccessPoint accessPoint = new WifiAccessPoint();
                accessPoint.setSsid(scanning.SSID);
                accessPoint.setRssi((double)scanning.level);
                addAccessPointToRemote(accessPoint);
                if (miner.database != null) {
                    miner.database.accessPointDao().insertAll(accessPoint);
                }
                rssi.add(rssiValue);
                rssiList.put(accessPointName, rssi);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context.getApplicationContext(),"SSID:" + "\n" + accessPointName + "\n" + "RSSI:" + rssiValue + "\n", Toast.LENGTH_SHORT).show();
                    }
                });
                //dbHelper.insert(accessPointName, rssiValue);
            }
        }
    }

    private void addAccessPointToRemote(@NonNull WifiAccessPoint accessPoint) {
        WifiRemoteDataSource.getInstance().addWifiAccessPoint(accessPoint, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.isSuccessful();
                // TODO: 2019-11-11
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO: 2019-11-11
                t.getMessage();
            }
        });
    }

}
