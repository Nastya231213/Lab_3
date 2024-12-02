package com.example.lab_3_weather_app;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout homeRl;
    private ProgressBar loadingPB;
    private ImageView backIV, iconIV, searchIV;
    private TextView cityName, tempertureTV, conditionTV;

    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private WeatherAdapter weatherRVAdapter;
    private ArrayList<WeatherModal> weatherModalArrayList;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityNameStr;

    private void getWeastherInfo(String city) {
        try {
            // Кодуємо місто перед передачею в запит
            String encodedCity = URLEncoder.encode(city, "UTF-8");

            String url = "https://api.weatherapi.com/v1/forecast.json?key=461377bec99745d29f794456242411&q=" + encodedCity + "&days=1&aqi=no&alerts=no";
            cityName.setText(city);
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(JSONObject response) {
                    loadingPB.setVisibility(View.GONE);
                    homeRl.setVisibility(View.VISIBLE);
                    weatherModalArrayList.clear();
                    try {
                        String temperature = response.getJSONObject("current").getString("temp_c");
                        tempertureTV.setText(getString(R.string.temperature) + " " + temperature);

                        String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                        String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                        Picasso.get().load("https:".concat(conditionIcon)).into(iconIV);
                        conditionTV.setText(getString(R.string.condition) + " " + condition);

                        JSONObject forecastObj = response.getJSONObject("forecast");
                        JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                        JSONArray hourArray = forecast0.getJSONArray("hour");
                        for (int i = 0; i < hourArray.length(); i++) {
                            JSONObject hourObj = hourArray.getJSONObject(i);

                            String time = hourObj.getString("time");
                            String temper = hourObj.getString("temp_c");
                            String img = hourObj.getJSONObject("condition").getString("icon");
                            String wind = hourObj.getString("wind_kph");

                            weatherModalArrayList.add(new WeatherModal(img, time, temper, wind));
                        }

                        weatherRVAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, getString(R.string.weather_data_error), Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(jsonObjectRequest);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Помилка кодування міста", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Задаємо українську локаль
        Locale locale = new Locale("uk");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        // Initializing Views
        homeRl = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityName = findViewById(R.id.idCityName);
        tempertureTV = findViewById(R.id.idTemperature);
        weatherRV = findViewById(R.id.idWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIcon);
        searchIV = findViewById(R.id.idSearch);
        conditionTV = findViewById(R.id.idCondition);

        weatherModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherAdapter(this, weatherModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        // Getting the city name from resources (using string resource)
        cityNameStr = getCityName();
        getWeastherInfo(cityNameStr);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEdt.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, getString(R.string.city_name_hint), Toast.LENGTH_SHORT).show();
                } else {
                    cityName.setText(city);
                    getWeastherInfo(city);
                }
            }
        });
    }

    private String getCityName() {
        return "Kyiv";
    }
}
