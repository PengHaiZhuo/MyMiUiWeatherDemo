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
        list.add(new WaterAndElectricMeterDetail("9999","29","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","15","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","25","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","23","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","20","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","18","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","16","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","25","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","24","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","16","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","12","1","2677","2020"));
        list.add(new WaterAndElectricMeterDetail("9999","24","1","2677","2020"));
        waterMeterView.setData(list);
    }
}
