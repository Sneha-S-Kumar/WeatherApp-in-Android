package com.example.weatherapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class WeatherDetailActivity extends AppCompatActivity {
    private TextView tvLocationDetail, tvDateDetail, tvTimeDetail, tvTemperatureDetail;
    private TextView tvDescriptionDetail, tvHumidity, tvPressure, tvWindSpeed, tvFeelsLike;
    private ImageView ivWeatherIconDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        initializeViews();

        WeatherItem weatherItem = (WeatherItem) getIntent().getSerializableExtra("weather_item");
        if (weatherItem != null) {
            displayWeatherDetails(weatherItem);
        }
    }

    private void initializeViews() {
        tvLocationDetail = findViewById(R.id.tvLocationDetail);
        tvDateDetail = findViewById(R.id.tvDateDetail);
        tvTimeDetail = findViewById(R.id.tvTimeDetail);
        tvTemperatureDetail = findViewById(R.id.tvTemperatureDetail);
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        tvHumidity = findViewById(R.id.tvHumidity);
      //  tvPressure = findViewById(R.id.tvPressure);
      //  tvWindSpeed = findViewById(R.id.tvWindSpeed);
        tvFeelsLike = findViewById(R.id.tvFeelsLike);
        ivWeatherIconDetail = findViewById(R.id.ivWeatherIconDetail);
    }

    private void displayWeatherDetails(WeatherItem item) {
        tvLocationDetail.setText(item.getLocation());
        tvDateDetail.setText(item.getFormattedDate());
        tvTimeDetail.setText(item.getFormattedTime());
        tvTemperatureDetail.setText(item.getFormattedTemperature());
        tvDescriptionDetail.setText(item.getDescription().toUpperCase());
        tvHumidity.setText("Humidity: " + item.getHumidity() + "%");
      //  tvPressure.setText("Pressure: " + item.getPressure() + " hPa");
     //   tvWindSpeed.setText("Wind Speed: " + item.getWindSpeed() + " m/s");
        tvFeelsLike.setText("Feels Like: " + Math.round(item.getFeelsLike()) + "°C");

        Glide.with(this)
                .load(item.getIconUrl())
                .into(ivWeatherIconDetail);
    }
}
