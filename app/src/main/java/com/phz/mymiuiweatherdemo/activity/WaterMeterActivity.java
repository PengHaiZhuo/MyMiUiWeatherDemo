package com.phz.mymiuiweatherdemo.activity;

import android.os.Bundle;

import com.phz.mymiuiweatherdemo.R;
import com.phz.mymiuiweatherdemo.bean.WaterAndElectricMeterDetail;
import com.phz.mymiuiweatherdemo.view.WaterMeterView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author haizhuo
 * @introduction
 */
public class WaterMeterActivity extends AppCompatActivity {
    private WaterMeterView waterMeterView;

    private List<WaterAndElectricMeterDetail> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_meter);
        waterMeterView=findViewById(R.id.water_view);
        initData();
    }

    private void initData() {
        list=new ArrayList<>();
        list.add(new WaterAndElectricMeterDetail("","","5","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","10","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","15","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","20","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","25","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","15","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","10","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","20","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","23","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","26","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","18","","","","",""));
        list.add(new WaterAndElectricMeterDetail("","","16","","","","",""));
        waterMeterView.setData(list);
    }
}
