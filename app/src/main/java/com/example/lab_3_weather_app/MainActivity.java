package com.example.lab_3_weather_app;

import android.annotation.SuppressLint;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
        String url = "https://api.weatherapi.com/v1/forecast.json?key=461377bec99745d29f794456242411&q=" + city + "&days=1&aqi=no&alerts=no";
        cityName.setText(city);
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRl.setVisibility(View.VISIBLE);
                weatherModalArrayList.clear();
                try {
                    String temperature =response.getJSONObject("current").getString("temp_c");
                    tempertureTV.setText(temperature);

                    int isDay =response.getJSONObject("current").getInt("is_day");

                    String condition =response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon =response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);
                    JSONObject forecastObj=response.getJSONObject("forecast");
                    JSONObject forecast0=forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray=forecast0.getJSONArray("hour");
                    for(int i=0;i<hourArray.length();i++){
                        JSONObject hourObj=hourArray.getJSONObject(i);

                        String time =hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img =hourObj.getJSONObject("condition").getString("icon");
                        String wind=hourObj.getString("wind_kph");

                        weatherModalArrayList.add(new WeatherModal(img,time,temper,wind));
                        for (WeatherModal weatherModal : weatherModalArrayList) {
                            Log.d("WeatherData", "WeatherModal: " + weatherModal.toString());
                        }
                    }

                    weatherRVAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Please enter valid city name..", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        homeRl = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityName = findViewById(R.id.idCityName);
        tempertureTV = findViewById(R.id.idTemperature);
        weatherRV = findViewById(R.id.idWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIcon);
        backIV=findViewById(R.id.idIVBack);
        searchIV = findViewById(R.id.idSearch);
        conditionTV=findViewById(R.id.idCondition);
        weatherModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherAdapter(this, weatherModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        cityNameStr=getCityName();
        getWeastherInfo(cityNameStr);
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city=cityEdt.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter city name",Toast.LENGTH_SHORT).show();
                }else{
                    cityName.setText(cityNameStr);
                    getWeastherInfo(city);

                }
            }
        });



    }


    private String getCityName() {
        String cityName = "Kyiv";

        return cityName;
    }
}