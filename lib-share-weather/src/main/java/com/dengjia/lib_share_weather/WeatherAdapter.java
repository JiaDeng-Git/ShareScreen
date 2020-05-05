package com.dengjia.lib_share_weather;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter {

    private String jsonString;

    private WeatherHolder weatherHolder;

    private List<Weather> weatherData = new ArrayList<>();

    public WeatherAdapter(String jsonString) {
        this.jsonString = jsonString;
    }

    private String timeFormat(String timeString){
        char[] arrays = timeString.toCharArray();
        return arrays[8] + arrays[9] + ":" + arrays[10] + arrays[11];
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
