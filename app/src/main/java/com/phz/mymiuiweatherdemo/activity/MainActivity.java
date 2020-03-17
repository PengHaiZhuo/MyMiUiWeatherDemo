package com.phz.mymiuiweatherdemo.activity;

import android.os.Bundle;

import com.phz.mymiuiweatherdemo.R;
import com.phz.mymiuiweatherdemo.bean.WeatherBean;
import com.phz.mymiuiweatherdemo.view.MiUiWeatherView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author haizhuo
 */
public class MainActivity extends AppCompatActivity {

    private List<WeatherBean> weatherBeanList;

    private MiUiWeatherView weatherView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherView=findViewById(R.id.weather_view);

        initData();
    }

    private void initData() {
        weatherBeanList=new ArrayList<>();
        weatherBeanList.add(new WeatherBean(WeatherBean.OVERCAST,10,"06:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.OVERCAST,15,"10:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.SUN,18,"14:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.CLOUDY,13,"18:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.CLOUDY,15,"22:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.RAIN,12,"02:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.RAIN,10,"06:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.RAIN,9,"10:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.RAIN,8,"14:00"));
        weatherBeanList.add(new WeatherBean(WeatherBean.RAIN,5,"18:00"));
        weatherView.setData(weatherBeanList);
    }
}
