package com.dengjia.lib_share_weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class WeatherManager {

    private Context context;

    private final String WEATHER_URL = "https://iweather.market.alicloudapi.com/address";

    private final String WEATHER_APP_CODE = "236a217563f6494fa5ba3a60afe9fd0f";

    private boolean autoSwitch = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public WeatherManager(Context context, Boolean autoSwitch) {
        this.context = context;
        this.autoSwitch = autoSwitch;

        if (!autoSwitch) {
            getWeatherData();
        } else {
            weatherAutoUpdata();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getWeatherData() {

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(WEATHER_URL).newBuilder();
        urlBuilder.addQueryParameter("city", "南昌");
        urlBuilder.addQueryParameter("needday", "24");
        urlBuilder.addQueryParameter("prov", "江西省");

        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Authorization", "APPCODE " + WEATHER_APP_CODE)
                .build();

        final Call call = client.newCall(request);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    weatherDataListener.onWeatherDataReceived(new WeatherAdapter(response.body().string()).getWeatherData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void weatherAutoUpdata() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        context.registerReceiver(mTimeReceiver, timeFilter);
    }

    private BroadcastReceiver mTimeReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar cal = Calendar.getInstance();
            int min = cal.get(Calendar.MINUTE);
            if (min == 0) {
                // TODO *******************************************************************************************
                try {
                    String dataString = null;
                    if (dataString != null && weatherDataListener != null) {
                        weatherDataListener.onWeatherDataReceived(new WeatherAdapter(dataString).getWeatherData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private WeatherDataListener weatherDataListener;

    public void addWeatherDataListener(WeatherDataListener weatherDataListener) {
        this.weatherDataListener = weatherDataListener;
    }

    public interface WeatherDataListener {
        void onWeatherDataReceived(List<Weather> weathers);
    }

}