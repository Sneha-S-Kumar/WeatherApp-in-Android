package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY="YOUR_API_KEY";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String PREFS_NAME = "WeatherPrefs";
    private static final String SAVED_LOCATIONS = "saved_locations";


    private EditText etLocation;
    private Button btnSearch;
    private ListView listSavedLocations;
    private RecyclerView rvWeatherForecast;
    private WeatherAdapter weatherAdapter;
    private List<WeatherItem> weatherList;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<String> locationAdapter;
    private List<String> savedLocationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupRecyclerView();
        setupSavedLocations();
        setupClickListeners();
    }

    private void setupSavedLocations() {
        savedLocationsList = new ArrayList<>();
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedLocationsList);
        listSavedLocations.setAdapter(locationAdapter);
        loadSavedLocations();
    }

    private void setupClickListeners() {
        btnSearch.setOnClickListener(v -> {
            String location = etLocation.getText().toString().trim();
            if (!location.isEmpty()) {
                fetchWeatherData(location);
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            }
        });

        listSavedLocations.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLocation = savedLocationsList.get(position);
            etLocation.setText(selectedLocation);
            fetchWeatherData(selectedLocation);
        });
    }

    private void fetchWeatherData(String location) {
        if (isNetworkAvailable()) {
            String url = BASE_URL + "?q=" + location + "&appid=" + API_KEY + "&units=metric";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                    null,
                    response -> {
                        parseWeatherData(response, location);
                        saveLocationToPrefs(location);
                    },
                    error -> {
                        Toast.makeText(this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
                        loadOfflineData(location);
                    });

            requestQueue.add(request);
        } else {
            Toast.makeText(this, "No internet connection. Loading offline data...", Toast.LENGTH_SHORT).show();
            loadOfflineData(location);
        }
    }

    private void parseWeatherData(JSONObject response, String location) {
        try {
            weatherList.clear();
            JSONArray list = response.getJSONArray("list");

            for (int i = 0; i < Math.min(list.length(), 40); i++) {   // 5days 8 forecast per day
                JSONObject forecast = list.getJSONObject(i);

                WeatherItem item = new WeatherItem();
                item.setLocation(location);
                item.setDateTime(forecast.getLong("dt"));
                item.setTemperature(forecast.getJSONObject("main").getDouble("temp"));
                item.setDescription(forecast.getJSONArray("weather").getJSONObject(0).getString("description"));
                item.setIcon(forecast.getJSONArray("weather").getJSONObject(0).getString("icon"));
                item.setHumidity(forecast.getJSONObject("main").getInt("humidity"));
                item.setPressure(forecast.getJSONObject("main").getDouble("pressure"));
                item.setWindSpeed(forecast.getJSONObject("wind").getDouble("speed"));
                item.setFeelsLike(forecast.getJSONObject("main").getDouble("feels_like"));

                weatherList.add(item);
            }

            weatherAdapter.notifyDataSetChanged();
            saveWeatherDataToPrefs(location, response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing weather data", Toast.LENGTH_SHORT).show();
        }
    }


    private void onWeatherItemClick(WeatherItem item) {
        Intent intent = new Intent(this, WeatherDetailActivity.class);
        intent.putExtra("weather_item", item);
        startActivity(intent);
    }

    private void saveLocationToPrefs(String location) {
        Set<String> locations = sharedPreferences.getStringSet(SAVED_LOCATIONS, new HashSet<>());
        locations.add(location);
        sharedPreferences.edit().putStringSet(SAVED_LOCATIONS, locations).apply();
        loadSavedLocations();
    }

    private void loadSavedLocations() {
        Set<String> locations = sharedPreferences.getStringSet(SAVED_LOCATIONS, new HashSet<>());
        savedLocationsList.clear();
        savedLocationsList.addAll(locations);
        locationAdapter.notifyDataSetChanged();
    }

    private void saveWeatherDataToPrefs(String location, String data) {
        sharedPreferences.edit().putString("weather_" + location, data).apply();
    }

    private void loadOfflineData(String location) {
        String offlineData = sharedPreferences.getString("weather_" + location, null);
        if (offlineData != null) {
            try {
                JSONObject response = new JSONObject(offlineData);
                parseWeatherData(response, location);
                Toast.makeText(this, "Loaded offline data for " + location, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "No offline data available for " + location, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No offline data available for " + location, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setupRecyclerView() {
        weatherList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(weatherList, this::onWeatherItemClick);
        rvWeatherForecast.setLayoutManager(new LinearLayoutManager(this));
        rvWeatherForecast.setAdapter(weatherAdapter);
    }

    private void initializeViews() {
        etLocation = findViewById(R.id.etLocation);
        btnSearch = findViewById(R.id.btnSearch);
        listSavedLocations = findViewById(R.id.lvSavedLocations);
        rvWeatherForecast = findViewById(R.id.rvWeatherForecast);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(this);
    }
}