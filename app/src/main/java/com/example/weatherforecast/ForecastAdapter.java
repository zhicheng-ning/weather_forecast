package com.example.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/***
 *@Authot: niko
 *@Date: Created in 19:12 2020/5/23
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
class ViewHolder{
    public TextView tv_date_title;
    public TextView tv_high_temp;
    public TextView tv_low_temp;

    public TextView tv_day_icon;
    public TextView tv_day_type;
    public TextView tv_day_wind_force;
    public TextView tv_day_wind_dir;

    public TextView tv_night_icon;
    public TextView tv_night_type;
    public TextView tv_night_wind_force;
    public TextView tv_night_wind_dir;

    View itemView;

    public ViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView can not be null");
        }
        this.itemView = itemView;
        tv_date_title = itemView.findViewById(R.id.tv_date_title);
        tv_high_temp = itemView.findViewById(R.id.tv_high_temp);
        tv_low_temp = itemView.findViewById(R.id.tv_low_temp);

        tv_day_icon = itemView.findViewById(R.id.tv_day_icon);
        tv_day_type = itemView.findViewById(R.id.tv_day_type);
        tv_day_wind_force = itemView.findViewById(R.id.tv_day_wind_force);
        tv_day_wind_dir = itemView.findViewById(R.id.tv_day_wind_dir);

        tv_night_icon = itemView.findViewById(R.id.tv_night_icon);
        tv_night_type = itemView.findViewById(R.id.tv_night_type);
        tv_night_wind_force = itemView.findViewById(R.id.tv_night_wind_force);
        tv_night_wind_dir = itemView.findViewById(R.id.tv_night_wind_dir);
    }
}

public class ForecastAdapter extends BaseAdapter {
    private List<WeatherForecast> forecastList;
    private LayoutInflater layoutInflater;
    private Context context;
    private int currentPis = -1;
    private ViewHolder holder = null;

    public ForecastAdapter(List<WeatherForecast> forecastList, Context context) {
        this.forecastList = forecastList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setFocusItemPos(int pos) {
        currentPis = pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return forecastList.size();
    }

    @Override
    public Object getItem(int position) {
        return forecastList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_date_title.setText(forecastList.get(position).getDate());
        holder.tv_high_temp.setText(forecastList.get(position).getTemp_max());
        holder.tv_low_temp.setText(forecastList.get(position).getTemp_min());

        String dayType = forecastList.get(position).getDay_type();
        Util.setWeatherIcon(context, dayType, holder.tv_day_icon);
        holder.tv_day_type.setText(dayType);
        holder.tv_day_wind_dir.setText("风向" + forecastList.get(position).getDay_wind_dir());
        holder.tv_day_wind_force.setText("风力" + forecastList.get(position).getDay_wind_force());

        String nightType = forecastList.get(position).getNight_type();
        Util.setWeatherIcon(context, nightType, holder.tv_night_icon);
        holder.tv_night_type.setText(forecastList.get(position).getNight_type());
        holder.tv_night_wind_dir.setText("风向" + forecastList.get(position).getNight_wind_dir());
        holder.tv_night_wind_force.setText("风力" + forecastList.get(position).getNight_wind_force());

        return convertView;
    }
}
