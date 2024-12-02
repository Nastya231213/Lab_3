package com.example.lab_3_weather_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherModal> weatherModalArrayList;

    public WeatherAdapter(Context context, ArrayList<WeatherModal> weatherModalArrayList) {
        this.context = context;
        this.weatherModalArrayList = weatherModalArrayList;

        Log.d("WeatherAdapter", "Адаптер створено, кількість елементів: " + weatherModalArrayList.size());
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Логування створення нового ViewHolder
        Log.d("WeatherAdapter", "Створення нового ViewHolder");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        WeatherModal modal = weatherModalArrayList.get(position);

        Log.d("WeatherAdapter", "Прив'язка даних для елемента на позиції: " + position);
        Log.d("WeatherAdapter", "Температура: " + modal.getTemperature());
        Log.d("WeatherAdapter", "Вітер: " + modal.getWind());
        Log.d("WeatherAdapter", "Час: " + modal.getTime());
        Log.d("WeatherAdapter", "Іконка погодного стану: " + modal.getIcon());

        holder.temperatureTV.setText(modal.getTemperature());
        Picasso.get().load("https:".concat(modal.getIcon())).into(holder.conditionIV);
        holder.windTV.setText(modal.getWind() + "Km/h");

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");

        try {
            Date time = input.parse(modal.getTime());
            holder.timeTV.setText(output.format(time));

            Log.d("WeatherAdapter", "Час відформатовано: " + output.format(time));
        } catch (ParseException e) {
            Log.e("WeatherAdapter", "Помилка парсингу часу: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("WeatherAdapter", "Кількість елементів у списку: " + weatherModalArrayList.size());
        return weatherModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView windTV, temperatureTV, timeTV;
        private ImageView conditionIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTV = itemView.findViewById(R.id.idTVWindSpeed);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            timeTV = itemView.findViewById(R.id.idTime);
            conditionIV = itemView.findViewById(R.id.idIVCondition);

            if (conditionIV == null) {
                Log.e("ViewHolder", "Елемент conditionIV не знайдений в макеті");
            } else {
                Log.d("ViewHolder", "Елемент conditionIV знайдений");
            }
        }
    }
}
