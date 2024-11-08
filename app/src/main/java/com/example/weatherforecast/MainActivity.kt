package com.example.weatherforecast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var locationHelper: LocationHelper

    private lateinit var currentTempText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var feelsLikeText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windSpeedText: TextView
    private lateinit var sunriseText: TextView
    private lateinit var sunsetText: TextView
    private lateinit var refreshButton: Button
    private lateinit var hourlyForecastButton: Button
    private lateinit var weeklyForecastButton: Button
    private lateinit var settingsButton: ImageButton

    private var isCelsius = true  // Flag for switching between Celsius and Fahrenheit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherRepository = WeatherRepository()
        locationHelper = LocationHelper(this)

        // Initialize views
        currentTempText = findViewById(R.id.current_temp)
        descriptionText = findViewById(R.id.weather_description)
        feelsLikeText = findViewById(R.id.feels_like)
        humidityText = findViewById(R.id.humidity)
        windSpeedText = findViewById(R.id.wind_speed)
        sunriseText = findViewById(R.id.sunrise_time)
        sunsetText = findViewById(R.id.sunset_time)
        refreshButton = findViewById(R.id.refresh_button)
        hourlyForecastButton = findViewById(R.id.button_hourly_forecast)
        weeklyForecastButton = findViewById(R.id.button_weekly_forecast)
        settingsButton = findViewById(R.id.button_settings)

        // Set up the settings button to navigate to the Settings activity
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Refresh weather data when the "Refresh" button is pressed
        refreshButton.setOnClickListener {
            fetchWeather()
        }

        // Hourly forecast button
        hourlyForecastButton.setOnClickListener {
            val intent = Intent(this, HourlyForecastActivity::class.java)
            startActivity(intent)
        }

        // Weekly forecast button
        weeklyForecastButton.setOnClickListener {
            val intent = Intent(this, WeeklyForecastActivity::class.java)
            startActivity(intent)
        }

        // First call to fetch weather data when the app starts
        fetchWeather()
    }

    private fun fetchWeather() {
        locationHelper.getLastLocation { location ->
            location?.let {
                val city = "Moscow" // Replace with dynamic city or coordinates
                lifecycleScope.launch {
                    try {
                        val weatherData = weatherRepository.getCurrentWeather(city)
                        weatherData?.let { data ->
                            // Update UI with the weather data
                            val temperature = if (isCelsius) {
                                "${data.main.temp} °C"
                            } else {
                                // Convert temperature to Fahrenheit
                                "${(data.main.temp * 9/5) + 32} °F"
                            }
                            currentTempText.text = temperature

                            // Weather description
                            descriptionText.text = data.weather.firstOrNull()?.description ?: "No data"

                            // Feels like temperature
                            feelsLikeText.text = "Feels like: ${data.main.feels_like} ${if (isCelsius) "°C" else "°F"}"

                            // Humidity
                            humidityText.text = "Humidity: ${data.main.humidity}%"

                            // Wind speed
                            windSpeedText.text = "Wind Speed: ${data.wind.speed} m/s"

                            // Sunrise and sunset times
                            val sunrise = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(data.sys.sunrise * 1000))
                            val sunset = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(data.sys.sunset * 1000))
                            sunriseText.text = "Sunrise: $sunrise"
                            sunsetText.text = "Sunset: $sunset"
                        } ?: run {
                            // Handle case when no data is returned
                            showError("Failed to load weather data")
                        }
                    } catch (e: Exception) {
                        // Handle errors (e.g., network issues, exceptions)
                        showError("Error fetching weather data: ${e.message}")
                    }
                }
            } ?: run {
                // Handle case when location is unavailable
                showError("Location not available")
            }
        }
    }

    private fun showError(message: String) {
        // Function to show an error message (e.g., using Toast)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
