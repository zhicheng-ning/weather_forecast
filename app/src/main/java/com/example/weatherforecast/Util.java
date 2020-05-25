package com.example.weatherforecast;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *@Authot: niko
 *@Date: Created in 15:50 2020/5/23
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 * 设置特殊字体的方法
 */
public class Util {
    public static void setWeatherIcon(Context context, String type, TextView tv) {
        Typeface weatherFront = Typeface.createFromAsset(context.getAssets(), "font/weather.ttf");
        tv.setTypeface(weatherFront);

        String icon = "";
        if (type != null) {
            if (type.trim().equals("晴")) {
                icon = context.getString(R.string.wi_day_sunny);
            }
            if (type.trim().equals("多云")) {
                icon = context.getString(R.string.wi_day_cloudy);
            }
            if (type.trim().equals("阴")) {
                icon = context.getString(R.string.wi_cloud);
            }
            if (type.trim().equals("小雨")) {
                icon = context.getString(R.string.wi_raindrops);
            }
            if (type.trim().equals("雷阵雨")) {
                icon = context.getString(R.string.wi_day_thunderstorm);
            }
            if (type.trim().equals("中雨")) {
                icon = context.getString(R.string.wi_rain_mix);
            }
            if (type.trim().equals("大雨")) {
                icon = context.getString(R.string.wi_rain);
            }
            if (type.trim().equals("暴雨")) {
                icon = context.getString(R.string.wi_thunderstorm);
            }
        }
        tv.setText(icon);
    }

}
