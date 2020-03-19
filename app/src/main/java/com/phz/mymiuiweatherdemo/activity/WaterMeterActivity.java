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
        list.add(new WaterAndElectricMeterDetail("333","29","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","15","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","25","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","23","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","20","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","18","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","16","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","25","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","24","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","16","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","12","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("333","24","1","2677","2020"));
        waterMeterView.setData(list);
    }
}
