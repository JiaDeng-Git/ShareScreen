package com.dengjia.lib_share_weather;

public class Weather {

    public String weather;
    public int temperature;
    public String temperatureStr; //温度的描述
    public String time; //时间值

    public Weather(String weather, int temperature, String time) {
        this.weather = weather;
        this.temperature = temperature;
        this.time = time;
        this.temperatureStr = temperature + "°";
    }

    public Weather(String weather, int temperature, String temperatureStr, String time) {
        this.weather = weather;
        this.temperature = temperature;
        this.temperatureStr = temperatureStr;
        this.time = time;
    }

}
