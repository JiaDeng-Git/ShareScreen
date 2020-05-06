package com.dengjia.lib_share_weather;

public class WeatherType {

    public static final String SUN = "晴";
    public static final String SUN_CLOUD = "多云";
    public static final String CLOUDY = "阴";
    public static final String SHOWER = "阵雨";
    public static final String THUNDER_SHOWER = "雷阵雨";
    public static final String THUNDER_SHOWER_WITH_HAIL = "雷阵雨并伴有冰雹";
    public static final String RAIN_SNOW = "雨夹雪";
    public static final String SMALL_RAIN = "小雨";
    public static final String MIDDLE_RAIN = "中雨";
    public static final String BIG_RAIN = "大雨";
    public static final String RUSH_RAIN = "暴雨";
    public static final String BIG_RUSH_RAIN = "大暴雨";
    public static final String HUGE_RUSH_RAIN = "特大暴雨";
    public static final String SNOW_SHOWER = "阵雪";
    public static final String SMALL_SNOW = "小雪";
    public static final String MIDDLE_SNOW = "中雪";
    public static final String BIG_SNOW = "大雪";
    public static final String RUSH_SNOW = "暴雪";
    public static final String FOG = "雾";
    public static final String ICE_RAIN = "冻雨";
    public static final String SAND_STORM = "沙尘暴";
    public static final String SMALL_TO_MIDLE_RAIN = "小到中雨";
    public static final String MIDDLE_TO_BIG_RAIN = "中到大雨";
    public static final String BIG_TO_RUSH_RAIN = "大到暴雨";
    public static final String RUSH_TO_BIGRUSH_RAIN = "暴雨到大暴雨";
    public static final String BIGRUSH_TO_HUGERUSH = "大暴雨到特大暴雨";
    public static final String SMALL_TO_MIDDLE_SNOW = "小到中雪";
    public static final String MIDDLE_TO_BIG_SNOW = "中到大雪";
    public static final String BIG_TO_HUGE_SNOW = "大到暴雪";
    public static final String FLY_ASH = "浮尘";
    public static final String BLOWING_SAND = "扬沙";
    public static final String BIG_SAND_STORM = "强沙尘暴";
    public static final String HAZE = "霾";
    public static final String NON_ERROR = "无";
    public static final String RAIN = "雨";
    public static final String SNOW = "雪";

    public static String[] getAllWeather(){
        String[] allWeather = {
                SUN,
                SUN_CLOUD,
                CLOUDY,
                SHOWER,
                THUNDER_SHOWER,
                THUNDER_SHOWER_WITH_HAIL,
                RAIN_SNOW,
                SMALL_RAIN,
                MIDDLE_RAIN,
                BIG_RAIN,
                RUSH_RAIN,
                BIG_RUSH_RAIN,
                HUGE_RUSH_RAIN,
                SNOW_SHOWER,
                SMALL_SNOW,
                MIDDLE_SNOW,
                BIG_SNOW,
                RUSH_SNOW,
                FOG,
                ICE_RAIN,
                SAND_STORM,
                SMALL_TO_MIDLE_RAIN,
                MIDDLE_TO_BIG_RAIN,
                BIG_TO_RUSH_RAIN,
                RUSH_TO_BIGRUSH_RAIN,
                BIGRUSH_TO_HUGERUSH,
                SMALL_TO_MIDDLE_SNOW,
                MIDDLE_TO_BIG_SNOW,
                BIG_TO_HUGE_SNOW,
                FLY_ASH,
                BLOWING_SAND,
                BIG_SAND_STORM,
                HAZE,
                NON_ERROR,
                RAIN,
                SNOW
        };
        return allWeather;
    }

}
