package com.aruba.wifirssi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aruba.wifirssi.adapter.WifiAccessPointListAdapter;
import com.aruba.wifirssi.data.AppDatabase;
import com.aruba.wifirssi.data.api.WifiRemoteDataSource;
import com.aruba.wifirssi.model.WifiAccessPoint;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private final static int PERMISSION_REQUEST_CODE_ACCESS_LOCATION = 800;

    private boolean hasScanStarted = false;
    private RecyclerView wifiApList;
    private RecyclerView.LayoutManager layoutManager;
    private WifiAccessPointListAdapter listAdapter;
    private ArrayList<WifiAccessPoint> wifiAccessPoints = new ArrayList<WifiAccessPoint>();

    private AppDatabase database;
    private WifiManager wifiManager;

    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                List<ScanResult> scanResults = wifiManager.getScanResults();
                unregisterReceiver(this);
                for (ScanResult scanning : scanResults) {
                    final WifiAccessPoint accessPoint = new WifiAccessPoint();
                    accessPoint.setSsid(scanning.SSID);
                    accessPoint.setRssi((double) scanning.level);
                    wifiAccessPoints.add(accessPoint);
                    if (database != null) {
                        database.accessPointDao().insertAll(accessPoint);
                    }
                    //// TODO: 2019-11-13 Get access point from db then send it to api
                    addAccessPointToRemote(accessPoint);
                    Log.d(TAG, "onReceive: " + "SSID:" + "\n" + accessPoint.getSsid() + "\n" + "RSSI:" + accessPoint.getRssi());
                }
                if (listAdapter != null) {
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.database = WifiApp.getDatabase();
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiApList = findViewById(R.id.wifi_ap_list);
        listAdapter = new WifiAccessPointListAdapter(this, wifiAccessPoints);
        layoutManager = new LinearLayoutManager(this);
        wifiApList.setLayoutManager(layoutManager);
        wifiApList.setAdapter(listAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiAccessPoints.clear();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE_ACCESS_LOCATION);
                    } else {
                        getAccessPointRssi();
                    }
                } else {
                    getAccessPointRssi();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(wifiScanReceiver, intentFilter);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE_ACCESS_LOCATION);
//            }
//        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(wifiScanReceiver);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSION_REQUEST_CODE_ACCESS_LOCATION == requestCode
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAccessPointRssi();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void getAccessPointRssi() {
        if (!hasScanStarted && wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                Log.d(TAG, "WiFi is disabled, Enabling it now!!");
                wifiManager.setWifiEnabled(true);
            }
            wifiManager.startScan();
            this.hasScanStarted = true;
            Snackbar.make(this.findViewById(android.R.id.content), "Wifi scanning started", Snackbar.LENGTH_LONG).setAction("Ok", null).show();
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
