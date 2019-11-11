package com.aruba.wifirssi.data.api;

import androidx.annotation.NonNull;

import com.aruba.wifirssi.model.WifiAccessPoint;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("wifiap")
    public Call<List<WifiAccessPoint>> getAccessPoints();

    @POST("wifiap")
    public Call<ResponseBody> addAccessPoint(@NonNull @Body WifiAccessPoint accessPoint);

}
