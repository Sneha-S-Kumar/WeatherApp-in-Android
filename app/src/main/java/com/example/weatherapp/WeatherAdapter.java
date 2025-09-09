package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<WeatherItem> weatherList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(WeatherItem item);
    }

    public WeatherAdapter(List<WeatherItem> weatherList, OnItemClickListener listener) {
        this.weatherList = weatherList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherItem item = weatherList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvTemperature, tvDescription;
        ImageView ivWeatherIcon;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
        }

        public void bind(WeatherItem item, OnItemClickListener listener) {
            tvDate.setText(item.getFormattedDate());
            tvTime.setText(item.getFormattedTime());
            tvTemperature.setText(item.getFormattedTemperature());
            tvDescription.setText(item.getDescription());


            Glide.with(itemView.getContext())
                    .load(item.getIconUrl())
                    .into(ivWeatherIcon);

            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
