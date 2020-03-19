package com.phz.mymiuiweatherdemo.bean;

/**
 * @author haizhuo
 * @introduction 水电表某个月的详情
 */
public class WaterAndElectricMeterDetail {

    public WaterAndElectricMeterDetail(String correction, String dosage,  String month, String totalReading, String year) {
        this.correction = correction;
        this.dosage = dosage;
        this.month = month;
        this.totalReading = totalReading;
        this.year = year;
    }

    /**
     * 修正读数
     */
    private String correction;
    /**
     * 用量
     */
    private String dosage;
    private String month;
    /**
     * 读数
     */
    private String totalReading;
    private String year;


    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotalReading() {
        return totalReading;
    }

    public void setTotalReading(String totalReading) {
        this.totalReading = totalReading;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
