package com.example.weatherforecast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/***
 *@Authot: niko
 *@Date: Created in 16:05 2020/5/23
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 * 核心类
 * 网络连接、数据获取、数据解析
 */
public class RemoteDataParser {
    private static String WTHRCDN = "http://wthrcdn.etouch.cn/WeatherApi?citykey=%s";

    /**
     * @param city_code 城市代码
     * @return 描述该城市天气的xml文件
     */
    public static String getRemoteData(String city_code) {
        BufferedReader reader=null;
        try {
            URL url = new URL(String.format(WTHRCDN, city_code));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             reader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer res = new StringBuffer(1024);
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                res.append(temp).append('\n');
            }

            return res.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static WeatherInfo PullParseXml(String xml) {
        WeatherInfo weatherInfo = new WeatherInfo();
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
//            获得xmlpullparser解析器
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
//            设置编码格式
            xmlPullParser.setInput(inputStream, "utf-8");
            WeatherForecast forecastInfo = null;
            WeatherIndex indexInfo = null;
//            是否是白天
            boolean isDay = false;
            String text = "";
            String tag = "";
//            获取解析到的事件类别，一直循环直到文档结束
            int evtType = xmlPullParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                tag = xmlPullParser.getName();
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        if (tag.equalsIgnoreCase("yesterday")
                                || tag.equalsIgnoreCase("weather")) {
                            forecastInfo = new WeatherForecast();
                        }
                        if (tag.equalsIgnoreCase("day_1") || tag.equalsIgnoreCase("day")) {
                            isDay = true;
                        }
                        if (tag.equalsIgnoreCase("night_1") || tag.equalsIgnoreCase("night")) {
                            isDay = false;
                        }
                        if (tag.equalsIgnoreCase("zhishu")) {
                            indexInfo = new WeatherIndex();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        if (text == null) text = "";
                        break;
                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase("city")) {
                            weatherInfo.setCity(text);
                        }
                        if (tag.equalsIgnoreCase("updatetime")) {
                            weatherInfo.setUpdate_time(text);
                        }
                        if (tag.equalsIgnoreCase("wendu")) {
                            weatherInfo.setCurrent_temp(text);
                        }
                        if (tag.equalsIgnoreCase("fengli") && forecastInfo == null) {
                            weatherInfo.setCurrent_wind_force(text);
                        }
                        if (tag.equalsIgnoreCase("shidu")) {
                            weatherInfo.setCurrent_humidity(text);
                        }
                        if (tag.equalsIgnoreCase("fengxiang") && forecastInfo == null) {
                            weatherInfo.setCurrent_wind_dir(text);
                        }
                        if (tag.equalsIgnoreCase("sunrise_1")) {
                            weatherInfo.setSun_rise(text);
                        }
                        if (tag.equalsIgnoreCase("sunset_1")) {
                            weatherInfo.setSun_set(text);
                        }
                        if (tag.equalsIgnoreCase("date_1") && forecastInfo != null) {
                            forecastInfo.setDate(text);
                        }
                        if (tag.equalsIgnoreCase("high_1") && forecastInfo != null) {
                            forecastInfo.setTemp_max(text);
                        }
                        if (tag.equalsIgnoreCase("low_1") && forecastInfo != null) {
                            forecastInfo.setTemp_min(text);
                        }
                        if (tag.equalsIgnoreCase("type_1") && forecastInfo != null && isDay == true) {
                            forecastInfo.setDay_type(text);
                        }
                        if (tag.equalsIgnoreCase("fx_1") && forecastInfo != null && isDay == true) {
                            forecastInfo.setDay_wind_dir(text);
                        }
                        if (tag.equalsIgnoreCase("fl_1") && forecastInfo != null && isDay == true) {
                            forecastInfo.setDay_wind_force(text);
                        }
                        if (tag.equalsIgnoreCase("type_1") && forecastInfo != null && isDay == false) {
                            forecastInfo.setNight_type(text);
                        }
                        if (tag.equalsIgnoreCase("fx_1") && forecastInfo != null && isDay == false) {
                            forecastInfo.setNight_wind_dir(text);
                        }
                        if (tag.equalsIgnoreCase("fl_1") && forecastInfo != null && isDay == false) {
                            forecastInfo.setNight_wind_force(text);
                        }

                        if (tag.equalsIgnoreCase("date") && forecastInfo != null) {
                            forecastInfo.setDate(text);
                        }
                        if (tag.equalsIgnoreCase("high") && forecastInfo != null) {
                            forecastInfo.setTemp_max(text);
                        }
                        if (tag.equalsIgnoreCase("low") && forecastInfo != null) {
                            forecastInfo.setTemp_min(text);
                        }
                        if (tag.equalsIgnoreCase("type") && forecastInfo != null && isDay == true) {
                            forecastInfo.setDay_type(text);
                        }
                        if (tag.equalsIgnoreCase("fengxiang") && forecastInfo != null && isDay == true) {
                            forecastInfo.setDay_wind_dir(text);
                        }
                        if (tag.equalsIgnoreCase("fengli") && forecastInfo != null && isDay == true) {
                            forecastInfo.setDay_wind_force(text);
                        }
                        if (tag.equalsIgnoreCase("type") && forecastInfo != null && isDay == false) {
                            forecastInfo.setNight_type(text);
                        }
                        if (tag.equalsIgnoreCase("fengxiang") && forecastInfo != null && isDay == false) {
                            forecastInfo.setNight_wind_dir(text);
                        }
                        if (tag.equalsIgnoreCase("fengli") && forecastInfo != null && isDay == false) {
                            forecastInfo.setNight_wind_force(text);
                        }
                        if (tag.equalsIgnoreCase("yestday") || tag.equalsIgnoreCase("weather")) {
                            weatherInfo.getForecastList().add(forecastInfo);
                        }

//                        生活气象指数
                        if (tag.equalsIgnoreCase("name")&&indexInfo!=null) {
                            indexInfo.setIndexName(text);
                        }
                        if (tag.equalsIgnoreCase("value")&&indexInfo!=null) {
                            indexInfo.setIndexValue(text);
                        }
                        if (tag.equalsIgnoreCase("detail")&&indexInfo!=null) {
                            indexInfo.setIndexDetail(text);
                        }
                        if (tag.equalsIgnoreCase("zhishu")) {
                            weatherInfo.getIndexList().add(indexInfo);
                        }
                        break;
                    default:
                        break;
                }
//                如果xml没有结束，则导航到下一个节点
                evtType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return weatherInfo;
    }

    public static WeatherInfo parseRemoteData(String city_code) {
        String xmlRes = getRemoteData(city_code);
        WeatherInfo weatherInfo = PullParseXml(xmlRes);
        return weatherInfo;
    }
}
