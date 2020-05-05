package com.dengjia.lib_share_weather;

import java.util.List;

public class WeatherHolder {


    /**
     * ret : 200
     * data : {"cityinfo":{"provinces":"江西","city":"南昌","area":"南昌","id":"101240101","prov_py":"jiangxi","city_py":"nanchang","qh":"0791","jb":"2","yb":"330000","area_py":"nanchang","area_short_code":"nc","lng":"115.892151","lat":"28.676493"},"hour":[{"time":"202005060100","temperature":"21","weather":"晴","weather_code":"00","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005060200","temperature":"21","weather":"晴","weather_code":"00","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060300","temperature":"21","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060400","temperature":"21","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060500","temperature":"21","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060600","temperature":"21","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060700","temperature":"22","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060800","temperature":"22","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060900","temperature":"23","weather":"雷阵雨","weather_code":"04","wind_power":"4-5级","wind_direction":"东北风"},{"time":"202005061000","temperature":"23","weather":"雷阵雨","weather_code":"04","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005061100","temperature":"24","weather":"雷阵雨","weather_code":"04","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005061200","temperature":"25","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061300","temperature":"25","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061400","temperature":"25","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061500","temperature":"26","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061600","temperature":"26","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061700","temperature":"26","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061800","temperature":"26","weather":"雷阵雨","weather_code":"04","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061900","temperature":"25","weather":"雷阵雨","weather_code":"04","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005062000","temperature":"25","weather":"雷阵雨","weather_code":"04","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005062100","temperature":"24","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005062200","temperature":"23","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东风"},{"time":"202005062300","temperature":"23","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东风"},{"time":"202005070000","temperature":"23","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005070100","temperature":"23","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东风"}]}
     * qt : 0.017
     */

    private int ret;
    private DataBean data;
    private double qt;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public double getQt() {
        return qt;
    }

    public void setQt(double qt) {
        this.qt = qt;
    }

    public static class DataBean {
        /**
         * cityinfo : {"provinces":"江西","city":"南昌","area":"南昌","id":"101240101","prov_py":"jiangxi","city_py":"nanchang","qh":"0791","jb":"2","yb":"330000","area_py":"nanchang","area_short_code":"nc","lng":"115.892151","lat":"28.676493"}
         * hour : [{"time":"202005060100","temperature":"21","weather":"晴","weather_code":"00","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005060200","temperature":"21","weather":"晴","weather_code":"00","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060300","temperature":"21","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060400","temperature":"21","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060500","temperature":"21","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060600","temperature":"21","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060700","temperature":"22","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060800","temperature":"22","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005060900","temperature":"23","weather":"雷阵雨","weather_code":"04","wind_power":"4-5级","wind_direction":"东北风"},{"time":"202005061000","temperature":"23","weather":"雷阵雨","weather_code":"04","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005061100","temperature":"24","weather":"雷阵雨","weather_code":"04","wind_power":"3-4级","wind_direction":"东北风"},{"time":"202005061200","temperature":"25","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061300","temperature":"25","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061400","temperature":"25","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061500","temperature":"26","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061600","temperature":"26","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061700","temperature":"26","weather":"阴","weather_code":"02","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061800","temperature":"26","weather":"雷阵雨","weather_code":"04","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005061900","temperature":"25","weather":"雷阵雨","weather_code":"04","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005062000","temperature":"25","weather":"雷阵雨","weather_code":"04","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005062100","temperature":"24","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005062200","temperature":"23","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东风"},{"time":"202005062300","temperature":"23","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东风"},{"time":"202005070000","temperature":"23","weather":"多云","weather_code":"01","wind_power":"<3级","wind_direction":"东北风"},{"time":"202005070100","temperature":"23","weather":"多云","weather_code":"01","wind_power":"3-4级","wind_direction":"东风"}]
         */

        private CityinfoBean cityinfo;
        private List<HourBean> hour;

        public CityinfoBean getCityinfo() {
            return cityinfo;
        }

        public void setCityinfo(CityinfoBean cityinfo) {
            this.cityinfo = cityinfo;
        }

        public List<HourBean> getHour() {
            return hour;
        }

        public void setHour(List<HourBean> hour) {
            this.hour = hour;
        }

        public static class CityinfoBean {
            /**
             * provinces : 江西
             * city : 南昌
             * area : 南昌
             * id : 101240101
             * prov_py : jiangxi
             * city_py : nanchang
             * qh : 0791
             * jb : 2
             * yb : 330000
             * area_py : nanchang
             * area_short_code : nc
             * lng : 115.892151
             * lat : 28.676493
             */

            private String provinces;
            private String city;
            private String area;
            private String id;
            private String prov_py;
            private String city_py;
            private String qh;
            private String jb;
            private String yb;
            private String area_py;
            private String area_short_code;
            private String lng;
            private String lat;

            public String getProvinces() {
                return provinces;
            }

            public void setProvinces(String provinces) {
                this.provinces = provinces;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProv_py() {
                return prov_py;
            }

            public void setProv_py(String prov_py) {
                this.prov_py = prov_py;
            }

            public String getCity_py() {
                return city_py;
            }

            public void setCity_py(String city_py) {
                this.city_py = city_py;
            }

            public String getQh() {
                return qh;
            }

            public void setQh(String qh) {
                this.qh = qh;
            }

            public String getJb() {
                return jb;
            }

            public void setJb(String jb) {
                this.jb = jb;
            }

            public String getYb() {
                return yb;
            }

            public void setYb(String yb) {
                this.yb = yb;
            }

            public String getArea_py() {
                return area_py;
            }

            public void setArea_py(String area_py) {
                this.area_py = area_py;
            }

            public String getArea_short_code() {
                return area_short_code;
            }

            public void setArea_short_code(String area_short_code) {
                this.area_short_code = area_short_code;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }
        }

        public static class HourBean {
            /**
             * time : 202005060100
             * temperature : 21
             * weather : 晴
             * weather_code : 00
             * wind_power : <3级
             * wind_direction : 东北风
             */

            private String time;
            private String temperature;
            private String weather;
            private String weather_code;
            private String wind_power;
            private String wind_direction;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getWeather_code() {
                return weather_code;
            }

            public void setWeather_code(String weather_code) {
                this.weather_code = weather_code;
            }

            public String getWind_power() {
                return wind_power;
            }

            public void setWind_power(String wind_power) {
                this.wind_power = wind_power;
            }

            public String getWind_direction() {
                return wind_direction;
            }

            public void setWind_direction(String wind_direction) {
                this.wind_direction = wind_direction;
            }
        }
    }
}
