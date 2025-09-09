package com.example.weatherapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherItem implements Serializable {
    private String location;
    private long dateTime;
    private double temperature;
    private String description;
    private String icon;
    private int humidity;
    private double pressure;
    private double windSpeed;
    private double feelsLike;

    // Constructors
    public WeatherItem() {}

    public WeatherItem(String location, long dateTime, double temperature, String description, String icon) {
        this.location = location;
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public long getDateTime() { return dateTime; }
    public void setDateTime(long dateTime) { this.dateTime = dateTime; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }

    // Utility methods
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(new Date(dateTime * 1000));
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(dateTime * 1000));
    }

    public String getFormattedTemperature() {
        return Math.round(temperature) + "°C";
    }

    public String getIconUrl() {
        return "https://openweathermap.org/img/w/" + icon + ".png";
    }
}
