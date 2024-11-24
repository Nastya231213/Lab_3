package com.example.lab_3_weather_app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.Manifest;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private RelativeLayout homeRl;
    private ProgressBar loadingPB;
    private ImageView backIV, iconIV, searchIV;
    private TextView cityName, tempertureTV, condition;

    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private WeatherAdapter weatherRVAdapter;
    private ArrayList<WeatherModal> weatherModalArrayList;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityNameStr;

    private void getWeastherInfo(String city) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=461377bec99745d29f794456242411&q=" + city + "&days=1&aqi=no&alerts=no";
        cityName.setText(cityNameStr);
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRl.setVisibility(View.VISIBLE);
                weatherModalArrayList.clear();
                try {
                    String temperature =response.getJSONObject("current").getString("temp_c");
                    tempertureTV.setText(temperature);
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
        searchIV = findViewById(R.id.idSearch);
        weatherModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherAdapter(this, weatherModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityNameStr=getCityName(location.getLatitude(),location.getLongitude());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted .. ", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Please provide the permissions",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double ourLatitude, double ourLongtitude) {
      String cityName = "Not Found";
       Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
       try{
           List<Address> addressess=gcd.getFromLocation(ourLatitude,ourLongtitude,10);
           for(Address address:addressess){
               if(address!=null){
                   String city=address.getLocality();
                   if(city!=null && !city.equals("")){
                       cityName=city;
                   }else{
                       Log.d("TAG","CITY NO FOUND");
                       Toast.makeText(this,"User City Not Found" ,Toast.LENGTH_SHORT).show();

                   }
               }
           }

       }catch(IOException e){
        e.printStackTrace();
       }
       return cityName;


   }
}
