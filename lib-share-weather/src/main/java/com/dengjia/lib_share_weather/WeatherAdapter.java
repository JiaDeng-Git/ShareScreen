package com.dengjia.lib_share_weather;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换类
 *
 * 把获得的Json数据转换为Weather类实例
 */
public class WeatherAdapter {

    private String jsonString;

    private WeatherHolder weatherHolder;

    private List<Weather> weatherData = new ArrayList<>();

    public WeatherAdapter(String jsonString) {
        this.jsonString = jsonString;
    }

    private String timeFormat(String timeString){
        return timeString.substring(8, 10) + ": " +timeString.substring(10, 12);
    }

    public List<Weather> getWeatherData() {

        weatherHolder = JSON.parseObject(this.jsonString, WeatherHolder.class);
        if (weatherHolder.getData().getHour() != null) {
            for (WeatherHolder.DataBean.HourBean hourBean : weatherHolder.getData().getHour()) {
                weatherData.add(new Weather(hourBean.getWeather(), Integer.valueOf(hourBean.getTemperature()), timeFormat(hourBean.getTime())));
            }
        }

        return weatherData;
    }
}
