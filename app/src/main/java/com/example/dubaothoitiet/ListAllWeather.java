package com.example.dubaothoitiet;


public class ListAllWeather {
    private String day;
    private String weather;
    private Float humid;
    private Float maxTemp;
    private Float minTemp;

    public ListAllWeather(String day, String weather, Float humid, Float maxTemp, Float minTemp) {
        this.day = day;
        this.weather = weather;
        this.humid = humid;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Float getHumid() {
        return humid;
    }

    public void setHumid(Float humid) {
        this.humid = humid;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Float minTemp) {
        this.minTemp = minTemp;
    }
}
