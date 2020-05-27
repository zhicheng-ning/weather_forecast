package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String default_city_code = "101250905";//南昌

    private TextView tv_address;
    private TextView tv_updatedAt;
    private TextView tv_status;
    private TextView tv_temp;
    private TextView tv_tempMin;
    private TextView tv_tempMax;
    private TextView tv_sunRise;
    private TextView tv_sunSet;
    private TextView tv_wind;
    private TextView tv_windDir;
    private TextView tv_humidity;
    private TextView tv_weatherIcon;

    private ForecastAdapter mListAdapter;
    private ListView mListView;

    private String[] weatherIndex = new String[]{};

    private List<CountryCode> codeList=new ArrayList<>();
    private String[] province = new String[]{"北京","江西"};//{"北京","江西"}
    private String[][] city = new String[][]{{"北京","海淀"},{"南昌","赣州"}};//{{"北京","海淀"},{"南昌","赣州"}}
    private String[][][] country = new String[][][]{{{"北京"},{"海淀"}},{{"南昌","新建","南昌县"},{"赣县","崇义"}}};//{{{"北京"},{"海淀"}},{{"南昌","新建","南昌县"},{"赣县","崇义"}}}

    private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    ArrayAdapter<String> countyAdapter = null;    //县级适配器
    int provincePosition = 0;//记录省号
    int cityPosition=0;//记录市号

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showDetail:
                new AlertDialog.Builder(MainActivity.this).setTitle("指数一览").setItems(weatherIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
            case R.id.about:
                AlertDialog.Builder infoDialog = new AlertDialog.Builder(MainActivity.this);
                infoDialog.setTitle("作者简介").setMessage("Author：逝不等琴生\nVersion：1.0\nAgency：NCU")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.exit:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("警告").setIcon(R.drawable.warning).setMessage("您是否确认退出？").setCancelable(false)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_address = findViewById(R.id.address);
        tv_updatedAt = findViewById(R.id.updated_at);
        tv_weatherIcon = findViewById(R.id.weather_icon);
        tv_status = findViewById(R.id.status);
        tv_temp = findViewById(R.id.temp);
        tv_tempMin = findViewById(R.id.temp_min);
        tv_tempMax = findViewById(R.id.temp_max);
        tv_sunRise = findViewById(R.id.sunrise);
        tv_sunSet = findViewById(R.id.sunset);
        tv_wind = findViewById(R.id.wind);
        tv_windDir = findViewById(R.id.wind_dir);
        tv_humidity = findViewById(R.id.humidity);

        mListView = findViewById(R.id.list_forecast);

//        读入城市代码文件，解析数据后存入到list集合codeList中
        InputStream codeStream = null;
        BufferedReader reader=null;
        CountryCode tempCode = null;
        //
        int cnt=0,indexProvince=0,indexCity=0,indexCountry=0;
        //
        try {
            codeStream = MainActivity.this.getResources().getAssets().open("city_code.txt");
             reader= new BufferedReader(new InputStreamReader(codeStream));
            String line = " ";
            String[] array;
            while ((line = reader.readLine()) != null) {
                tempCode = new CountryCode();
                array = line.split("\t");
                tempCode.setProvince(array[0]);
                tempCode.setCity(array[1]);
                tempCode.setCountry(array[2]);
                tempCode.setCode(array[3]);
                codeList.add(tempCode);
/*//
                if(cnt==0){//不是同一个省
                    province[indexProvince]=codeList.get(cnt).getProvince();
                    city[indexProvince][indexCity] = codeList.get(cnt).getCity();
                    country[indexProvince][indexCity][indexCountry] = codeList.get(cnt).getCountry();
                } else if (codeList.get(cnt).getProvince().equals(codeList.get(cnt - 1).getProvince()) == false) {//不是同一个省
                    indexProvince++;
                    province[indexProvince]=codeList.get(cnt).getProvince();
                    indexCity=0;
                    indexCountry=0;
                    city[indexProvince][indexCity] = codeList.get(cnt).getCity();
                    country[indexProvince][indexCity][indexCountry] = codeList.get(cnt).getCountry();
                } else {
                    if(codeList.get(cnt).getCity().equals(codeList.get(cnt-1).getCity())==false){//不是同一个县
                        indexCity++;
                        city[indexProvince][indexCity] = codeList.get(cnt).getCity();
                        indexCountry=0;
                        country[indexProvince][indexCity][indexCountry] = codeList.get(cnt).getCountry();
                    }else {//同一个县
                        indexCountry++;
                        country[indexProvince][indexCity][indexCountry] = codeList.get(cnt).getCountry();
                    }
                }
            cnt++;*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (codeStream != null) {
                try {
                    codeStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
/*
// 为  province ，city, country数组赋值
        Set<String> citySet = new LinkedHashSet<>();
        Set<String> countrySet = new LinkedHashSet<>();
        int provinceCnt=0;
        for (int i = 0; i < codeList.size(); i++) {
//            出现新的省份
            if (i == 0 || !codeList.get(i).getProvince().equals(codeList.get(i - 1).getProvince())) {
                citySet.clear();//新的省，清空二级列表中的市
                countrySet.clear();//清空县

                citySet.add(codeList.get(i).getCity());
                city[provinceCnt][citySet.size() - 1] = codeList.get(i).getCity();//赋值市
                countrySet.add(codeList.get(i).getCountry());
                country[provinceCnt][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();//赋值县
                province[provinceCnt++] = codeList.get(i).getProvince();
            } else {//同一省份
                citySet.add(codeList.get(i).getCity());
                city[provinceCnt][citySet.size()-1]=codeList.get(i).getCity();
                if (codeList.get(i).getCity().equals(codeList.get(i - 1).getCity())) {
                    //同一个市
                    countrySet.add(codeList.get(i).getCountry());
                    country[provinceCnt][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();
                } else {
                    countrySet.clear();
                    countrySet.add(codeList.get(i).getCountry());
                    country[provinceCnt][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();
                }
            }
        }*/

//        设置spinner
        setSpinner();
//        异步任务根据城市代码获取天气数据
        new weatherTask().execute();
    }

    private void setSpinner() {
        provinceSpinner = findViewById(R.id.spin_province);
        citySpinner = findViewById(R.id.spin_city);
        countySpinner = findViewById(R.id.spin_county);
        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, province);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第0个值

        cityAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, city[0]);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0, true);  //默认选中第0个

        countyAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, country[0][0]);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setSelection(0, true);
        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号

                //将地级适配器的值改变为city[position]中的值
                cityAdapter = new ArrayAdapter<String>(
                        MainActivity.this, android.R.layout.simple_spinner_item, city[position]);
                // 设置二级下拉列表的选项内容适配器
                citySpinner.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                countyAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, country[provincePosition][position]);
                countySpinner.setAdapter(countyAdapter);
                cityPosition=position;//记录当前市号
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

//        县级监听
        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String countryName=country[provincePosition][cityPosition][position];
                for(int i=0;i<codeList.size();i++){
                    CountryCode cc=codeList.get(i);
                    //选择了县
                    if(cc.getProvince().equals(province[provincePosition])
                            &&cc.getCity().equals(city[provincePosition][cityPosition])
                            &&cc.getCountry().equals(countryName)){
                        default_city_code=cc.getCode();
//                        更新界面
                        new weatherTask().execute();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    class weatherTask extends AsyncTask<WeatherInfo, Void, WeatherInfo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            显示进度条时，主界面隐藏
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        /**
         * @param weatherInfos
         * @return 天气信息
         */
        @Override
        protected WeatherInfo doInBackground(WeatherInfo... weatherInfos) {
            WeatherInfo weatherInfo = RemoteDataParser.parseRemoteData(default_city_code);
            return weatherInfo;
        }

        @Override
        protected void onPostExecute(WeatherInfo res) {
            super.onPostExecute(res);
            try {
                tv_address.setText(res.getCity());
                Time t = new Time();
                t.setToNow();//获取系统时间
                int year = t.year;
                int month = t.month + 1;
                int day = t.monthDay;
                String updateTime = "更新" + year + "-" + month + "-" + day + " " + res.getUpdate_time();
                tv_updatedAt.setText(updateTime);

                String dayType = res.getForecastList().get(0).getDay_type();
                Util.setWeatherIcon(getApplicationContext(), dayType, tv_weatherIcon);
                tv_status.setText(dayType);

                Typeface light = Typeface.createFromAsset(getAssets(), "font/Quicksand-Light.otf");
                tv_temp.setTypeface(light);
                tv_temp.setText(res.getCurrent_temp() + "°");
                tv_tempMin.setText(res.getForecastList().get(0).getTemp_min());
                tv_tempMax.setText(res.getForecastList().get(0).getTemp_max());

                tv_sunRise.setText(res.getSun_rise());
                tv_sunSet.setText(res.getSun_set());
                tv_wind.setText(res.getCurrent_wind_force());
                tv_windDir.setText(res.getCurrent_wind_dir());
                tv_humidity.setText(res.getCurrent_humidity());

                mListAdapter = new ForecastAdapter(res.getForecastList(), MainActivity.this);
                mListView.setAdapter(mListAdapter);
//                气象指数
                List<WeatherIndex> indexList = res.getIndexList();
                weatherIndex = new String[indexList.size()];
                for (int i = 0; i < indexList.size(); i++) {
                    String temp = indexList.get(i).getIndexName() + ":\n" + indexList.get(i).getIndexValue() + "\n" + indexList.get(i).getIndexDetail() + "\n";
                    weatherIndex[i] = temp;
                }
//            隐藏进度条，显示主界面
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (Exception e) {
                findViewById(R.id.loader).setVisibility(View.VISIBLE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }
}
