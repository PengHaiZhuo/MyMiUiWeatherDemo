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
        list.add(new WaterAndElectricMeterDetail("9999.00","29.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","15.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","25.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","23.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","20.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","18.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","16.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","25.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","24.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","16.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","12.00","1","2677.00.","2020"));
        list.add(new WaterAndElectricMeterDetail("9999.00","24.00","1","2677.00.","2020"));
        waterMeterView.setData(list);
    }
}
