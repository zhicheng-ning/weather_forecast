package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;
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

    private List<CountryCode> codeList;


    private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    ArrayAdapter<String> countyAdapter = null;    //县级适配器
    static int provincePosition = 3;

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


        /*try {
            InputStream codeStream = getAssets().open("city_code.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(codeStream));
            String line = "";
            String[] arry;
            codeList = new ArrayList<>();
            CountryCode tempCode=null;
            while ((line = reader.readLine()) != null) {
                arry = line.split("\t");
                tempCode = new CountryCode();
                tempCode.setProvince(arry[0]);
                tempCode.setCity(arry[1]);
                tempCode.setCountry(arry[2]);
                tempCode.setCode(arry[3]);
                codeList.add(tempCode);
            }
            for (int i = 0; i < codeList.size(); i++) {
                Log.d("codeList:", codeList.get(i).getCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        new weatherTask().execute();
        //            获得城市代码列表对象
        try {
            InputStream codeStream = getAssets().open("city_code.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(codeStream));
            String line = "";
            String[] array;
            codeList = new ArrayList<>();
            CountryCode tempCode=null;

            String[] province = new String[]{};
            String[][] city = new String[][]{{}};
            String[][][] country = new String[][][]{{{}}};


            Set<String> citySet = new HashSet<>();
            Set<String> countrySet = new HashSet<>();
            while ((line = reader.readLine()) != null) {
                array = line.split("\t");
                tempCode = new CountryCode();
                tempCode.setProvince(array[0]);
                tempCode.setCity(array[1]);
                tempCode.setCountry(array[2]);
                tempCode.setCode(array[3]);
                codeList.add(tempCode);
            }
            Log.d("codeListNum", String.valueOf(codeList.size()));
            int cnt1=0;//表示省的数量
            for (int i = 0; i < codeList.size(); i++) {
                //出现新的省份
                if (i == 0 || !codeList.get(i).getProvince().equals(codeList.get(i - 1).getProvince())) {
                    citySet.clear();//新的省，因此清空所以的市
                    countrySet.clear();// 新的省，所以清空所有的县

                    citySet.add(codeList.get(i).getCity());//添加市
                    city[cnt1][citySet.size()-1] = codeList.get(i).getCity();//赋值市
                    countrySet.add(codeList.get(i).getCountry());//添加县
                    country[cnt1][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();//赋值县
                    province[cnt1++] = codeList.get(i).getProvince();//赋值省

                }else {
                    citySet.add(codeList.get(i).getCity());//添加市
                    city[cnt1][citySet.size()-1] = codeList.get(i).getCity();//赋值市
//                        同一个市
                    if (codeList.get(i).getCity().equals(codeList.get(i - 1).getCity())) {
                        countrySet.add(codeList.get(i).getCountry());//添加县
                        country[cnt1][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();//赋值县
                    } else {//非同一个市,清空所有县
                        countrySet.clear();
                        countrySet.add(codeList.get(i).getCountry());//添加县
                        country[cnt1][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();//赋值县
                    }
                }
            }
            Log.d("provinceNum:", String.valueOf(province.length));

        } catch (IOException e) {
            e.printStackTrace();
        }

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

/*//            获得城市代码列表对象
            try {
                InputStream codeStream = getAssets().open("city_code.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(codeStream));
                String line = "";
                String[] array;
                codeList = new ArrayList<>();
                CountryCode tempCode=null;

                String[] province = new String[]{};
                String[][] city = new String[][]{{}};
                String[][][] country = new String[][][]{{{}}};

                Set<String> provinceSet = new HashSet();
                Set<String> citySet = new HashSet();
                Set<String> countrySet = new HashSet();
                while ((line = reader.readLine()) != null) {
                    array = line.split("\t");
                    tempCode = new CountryCode();
                    tempCode.setProvince(array[0]);
                    tempCode.setCity(array[1]);
                    tempCode.setCountry(array[2]);
                    tempCode.setCode(array[3]);
                    codeList.add(tempCode);
                }
                int cnt1=0;//表示省的数量
                for (int i = 0; i < codeList.size(); i++) {
                    //出现新的省份
                    if (i == 0 || codeList.get(i).getProvince() != codeList.get(i - 1).getProvince()) {
                        citySet.clear();//新的省，因此清空所以的市
                        countrySet.clear();// 新的省，所以清空所有的县

                        citySet.add(codeList.get(i).getCity());//添加市
                        city[cnt1][citySet.size()-1] = codeList.get(i).getCity();//赋值市
                        countrySet.add(codeList.get(i).getCountry());//添加县
                        country[cnt1][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();//赋值县
                        province[cnt1++] = codeList.get(i).getProvince();//赋值省

                    }else {
                        citySet.add(codeList.get(i).getCity());//添加市
                        city[cnt1][citySet.size()-1] = codeList.get(i).getCity();//赋值市
//                        同一个市
                        if (codeList.get(i).getCity() == codeList.get(i - 1).getCity()) {
                            countrySet.add(codeList.get(i).getCountry());//添加县
                            country[cnt1][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();//赋值县
                        } else {//非同一个市,清空所有县
                            countrySet.clear();
                            countrySet.add(codeList.get(i).getCountry());//添加县
                            country[cnt1][citySet.size() - 1][countrySet.size() - 1] = codeList.get(i).getCountry();//赋值县
                        }
                    }
                }
                Log.d("provinceNum:", String.valueOf(province.length));

            } catch (IOException e) {
                e.printStackTrace();
            }*/

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

                //


            } catch (Exception e) {
                findViewById(R.id.loader).setVisibility(View.VISIBLE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }
}
