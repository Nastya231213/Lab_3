package com.example.lab_3_weather_app;

public class WeatherModal {
    private String icon;
    private String time;
    private String temperature;
    private String wind;

    public WeatherModal(String icon, String time, String temperature, String wind) {
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
        this.wind = wind;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
