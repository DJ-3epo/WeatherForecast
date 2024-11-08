package com.example.weatherforecast

class WeatherRepository {
    private val apiKey = "8471cebbde7439134995e7d310a5385c"

    suspend fun getCurrentWeather(city: String): WeatherResponse? {
        val response = RetrofitInstance.weatherApi.getCurrentWeather(city, apiKey)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getForecast(city: String): ForecastResponse? {
        val response = RetrofitInstance.weatherApi.getForecast(city, apiKey)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}