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
        list.add(new WaterAndElectricMeterDetail("","333","5","","1","2677","",""));
        list.add(new WaterAndElectricMeterDetail("","333","10","","2","2687","",""));
        list.add(new WaterAndElectricMeterDetail("","333","15","","3","2702","",""));
        list.add(new WaterAndElectricMeterDetail("","333","20","","4","2727","",""));
        list.add(new WaterAndElectricMeterDetail("","333","25","","5","2727","",""));
        list.add(new WaterAndElectricMeterDetail("","333","15","","6","2727","",""));
        list.add(new WaterAndElectricMeterDetail("","333","10","","7","2727","",""));
        list.add(new WaterAndElectricMeterDetail("","333","20","","8","2727","",""));
        list.add(new WaterAndElectricMeterDetail("","333","23","","9","2727","",""));
        list.add(new WaterAndElectricMeterDetail("","333","26","","10","2727","",""));
        list.add(new WaterAndElectricMeterDetail("","333","18","","11","2727","",""));
        list.add(new WaterAndElectricMeterDetail("","333","16","","12","2727","",""));
        waterMeterView.setData(list);
    }
}
