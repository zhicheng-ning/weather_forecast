package com.example.weatherforecast;

import java.util.ArrayList;
import java.util.List;

/***
 *@Authot: niko
 *@Date: Created in 15:33 2020/5/23
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class WeatherInfo {
    private String city;//城市
    private String update_time;//更新时间
    private String current_wind_force;//风力
    private String current_wind_dir;//风向
    private String sun_rise;//日出
    private String sun_set;//日落
    private String current_temp;//当前温度
    private String current_humidity;//当前湿度
    List<WeatherForecast> forecastList;
    List<WeatherIndex> indexList;

    public List<WeatherIndex> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<WeatherIndex> indexList) {
        this.indexList = indexList;
    }

    public WeatherInfo() {
        forecastList = new ArrayList<>();
        indexList = new ArrayList<>();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCurrent_wind_force() {
        return current_wind_force;
    }

    public void setCurrent_wind_force(String current_wind_force) {
        this.current_wind_force = current_wind_force;
    }

    public String getCurrent_wind_dir() {
        return current_wind_dir;
    }

    public void setCurrent_wind_dir(String current_wind_dir) {
        this.current_wind_dir = current_wind_dir;
    }

    public String getSun_set() {
        return sun_set;
    }

    public void setSun_set(String sun_set) {
        this.sun_set = sun_set;
    }

    public String getSun_rise() {
        return sun_rise;
    }

    public void setSun_rise(String sun_rise) {
        this.sun_rise = sun_rise;
    }

    public String getCurrent_temp() {
        return current_temp;
    }

    public void setCurrent_temp(String current_temp) {
        this.current_temp = current_temp;
    }

    public String getCurrent_humidity() {
        return current_humidity;
    }

    public void setCurrent_humidity(String current_humidity) {
        this.current_humidity = current_humidity;
    }

    public List<WeatherForecast> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<WeatherForecast> forecastList) {
        this.forecastList = forecastList;
    }
}
