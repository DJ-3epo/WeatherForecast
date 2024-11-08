package com.example.weatherforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class HourlyForecast(val time: String, val iconRes: Int, val temperature: String)

class HourlyForecastAdapter(private val forecastList: List<HourlyForecast>) :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_forecast_item, parent, false)
        return HourlyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.timeText.text = forecast.time
        holder.weatherIcon.setImageResource(forecast.iconRes)
        holder.temperatureText.text = forecast.temperature
    }

    override fun getItemCount(): Int = forecastList.size

    class HourlyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeText: TextView = itemView.findViewById(R.id.hour_text)
        val weatherIcon: ImageView = itemView.findViewById(R.id.weather_icon)
        val temperatureText: TextView = itemView.findViewById(R.id.temperature_text)
    }
}
