package com.aruba.wifirssi.data.api;

import androidx.annotation.NonNull;

import com.aruba.wifirssi.model.WifiAccessPoint;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class WifiRemoteDataSource {

    private static final String ENDPOINT = "https://mysterious-brushlands-97954.herokuapp.com/";

    private static WifiRemoteDataSource sInstance;
    // Retrofit service, used to call endpoints
    private final ApiService wifiApiService;

    public static WifiRemoteDataSource getInstance() {
        if (sInstance == null) {
            synchronized (WifiRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new WifiRemoteDataSource();
                }
            }
        }
        return sInstance;
    }

    private WifiRemoteDataSource() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
                .connectTimeout((60 * 1000), TimeUnit.MILLISECONDS)
                .readTimeout((60 * 1000), TimeUnit.MILLISECONDS)
                .writeTimeout((60 * 1000), TimeUnit.MILLISECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(okhttpBuilder.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        wifiApiService = retrofit.create(ApiService.class);
    }

    public void getWifiAccessPoints(@NonNull Callback<List<WifiAccessPoint>> callback) {
        wifiApiService.getAccessPoints().enqueue(callback);
    }

    public void addWifiAccessPoint(@NonNull WifiAccessPoint accessPoint, @NonNull Callback<ResponseBody> callback) {
        wifiApiService.addAccessPoint(accessPoint).enqueue(callback);
    }

}
