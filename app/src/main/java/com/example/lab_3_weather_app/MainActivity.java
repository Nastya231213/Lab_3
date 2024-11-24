package com.example.lab_3_weather_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout homeRl;
    private ProgressBar loadingPB;
    private ImageView backIV,iconIV,searchIV;
    private TextView cityName,tempertureTV,condition;

    private RecyclerView weatherRV;
    private TextInputLayout cityEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeRl=findViewById(R.id.idRLHome);
        loadingPB=findViewById(R.id.idPBLoading);
       cityName=findViewById(R.id.idCityName);
        tempertureTV=findViewById(R.id.idTemperature);
        weatherRV=findViewById(R.id.idWeather);
        cityEdt=findViewById(R.id.idEdtCity);
        backIV=findViewById(R.id.idIVBack);
        iconIV=findViewById(R.id.idIcon);
        searchIV=findViewById(R.id.idSearch);

    }
}