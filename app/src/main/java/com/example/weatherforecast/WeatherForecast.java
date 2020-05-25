package com.example.weatherforecast;

/***
 *@Authot: niko
 *@Date: Created in 15:33 2020/5/23
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 *天气的数据
 */
public class WeatherForecast {
    private String date;//日期
    private String temp_max;//最高温
    private String temp_min;//最低温
    private String day_type;//白天天气类型
    private String day_wind_force;//风力
    private String day_wind_dir;//风向
    private String night_type;//夜晚天气类型
    private String night_wind_force;
    private String night_wind_dir;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getDay_type() {
        return day_type;
    }

    public void setDay_type(String day_type) {
        this.day_type = day_type;
    }

    public String getDay_wind_force() {
        return day_wind_force;
    }

    public void setDay_wind_force(String day_wind_force) {
        this.day_wind_force = day_wind_force;
    }

    public String getDay_wind_dir() {
        return day_wind_dir;
    }

    public void setDay_wind_dir(String day_wind_dir) {
        this.day_wind_dir = day_wind_dir;
    }

    public String getNight_type() {
        return night_type;
    }

    public void setNight_type(String night_type) {
        this.night_type = night_type;
    }

    public String getNight_wind_force() {
        return night_wind_force;
    }

    public void setNight_wind_force(String night_wind_force) {
        this.night_wind_force = night_wind_force;
    }

    public String getNight_wind_dir() {
        return night_wind_dir;
    }

    public void setNight_wind_dir(String night_wind_dir) {
        this.night_wind_dir = night_wind_dir;
    }
}
