package com.dengjia.lib_share_weather;

public enum WeatherType {

    SUN("00","晴"),
    SUN_CLOUD("01","多云"),
    CLOUDY("02","阴"),
    SHOWER("03","阵雨"),
    THUNDER_SHOWER("04","雷阵雨"),
    THUNDER_SHOWER_WITH_HAIL("05","雷阵雨并伴有冰雹"),
    RAIN_SNOW("06","雨夹雪"),
    SMALL_RAIN("07","小雨"),
    MIDDLE_RAIN("08","中雨"),
    BIG_RAIN("09","大雨"),
    RUSH_RAIN("10","暴雨"),
    BIG_RUSH_RAIN("11","大暴雨"),
    HUGE_RUSH_RAIN("12","特大暴雨"),
    SNOW_SHOWER("13","阵雪"),
    SMALL_SNOW("14","小雪"),
    MIDDLE_SNOW("15","中雪"),
    BIG_SNOW("16","大雪"),
    RUSH_SNOW("17","暴雪"),
    FOG("18","雾"),
    ICE_RAIN("19","冻雨"),
    SAND_STORM("20","沙尘暴"),
    SMALL_TO_MIDLE_RAIN("21","小到中雨"),
    MIDDLE_TO_BIG_RAIN("22","中到大雨"),
    BIG_TO_RUSH_RAIN("23","大到暴雨"),
    RUSH_TO_BIGRUSH_RAIN("24","暴雨到大暴雨"),
    BIGRUSH_TO_HUGERUSH("25","大暴雨到特大暴雨"),
    SMALL_TO_MIDDLE_SNOW("26","小到中雪"),
    MIDDLE_TO_BIG_SNOW("27","中到大雪"),
    BIG_TO_HUGE_SNOW("28","大到暴雪"),
    FLY_ASH("29","浮尘"),
    BLOWING_SAND("30","扬沙"),
    BIG_SAND_STORM("31","强沙尘暴"),
    HAZE("53","霾"),
    NON_ERROR("99","无"),
    RAIN("301","雨"),
    SNOW("302","雪");

    private String picName;
    private String weatherName;

    WeatherType(String picName, String weatherName){
        this.picName = picName;
        this.weatherName = weatherName;
    }

    public String getPicName() {
        return picName;
    }

    public String getWeatherName() {
        return weatherName;
    }
}
