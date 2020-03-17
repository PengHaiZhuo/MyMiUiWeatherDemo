package com.phz.mymiuiweatherdemo.bean;

/**
 * @author haizhuo
 * @introduction 按年搜索某个水表或电表的所有的当年的数据中的{某一个月详情}
 */
public class WaterAndElectricMeterDetail {

    public WaterAndElectricMeterDetail() {
    }

    public WaterAndElectricMeterDetail(String commonReading, String correction, String dosage, String meterCode, String month, String totalReading, String vallyReading, String year) {
        this.commonReading = commonReading;
        this.correction = correction;
        this.dosage = dosage;
        this.meterCode = meterCode;
        this.month = month;
        this.totalReading = totalReading;
        this.vallyReading = vallyReading;
        this.year = year;
    }

    private String commonReading;
    /**
     * 修正读数
     */
    private String correction;
    /**
     * 用量
     */
    private String dosage;
    private String meterCode;
    private String month;
    /**
     * 读数
     */
    private String totalReading;
    private String vallyReading;
    private String year;

    public String getCommonReading() {
        return commonReading;
    }

    public void setCommonReading(String commonReading) {
        this.commonReading = commonReading;
    }

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

    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
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

    public String getVallyReading() {
        return vallyReading;
    }

    public void setVallyReading(String vallyReading) {
        this.vallyReading = vallyReading;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
