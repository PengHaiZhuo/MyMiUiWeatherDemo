package com.phz.mymiuiweatherdemo.bean;

/**
 * @author:haizhuo
 */
public class WeatherBean {

    public static final String SUN="晴";
    public static final String OVERCAST="阴";
    public static final String CLOUDY="多云";
    public static final String RAIN="雨";
    public static final String SNOW="雪";
    public static final String THUNDER="雷";

    /**
     * 天气
     */
    private String weather;

    /**
     * 温度（℃）
     */
    private int temperature;

    /**
     * 温度描述
     */
    private String temperatureDescription;

    /**
     * 时间(00：00)
     */
    private String time;

    public WeatherBean(String weather,int temperature,String temperatureDescription,String time) {
        this.weather=weather;
        this.temperature=temperature;
        this.temperatureDescription=temperatureDescription;
        this.time=time;
    }

    public WeatherBean(String weather, int temperature, String time) {
        this.weather = weather;
        this.temperature = temperature;
        this.temperatureDescription=temperature+"℃";
        this.time = time;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getTemperatureDescription() {
        return temperatureDescription;
    }

    public void setTemperatureDescription(String temperatureDescription) {
        this.temperatureDescription = temperatureDescription;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
