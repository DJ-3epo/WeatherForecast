package com.example.weatherforecast

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tempUnitGroup: RadioGroup
    private lateinit var pushNotificationsSwitch: Switch
    private lateinit var themeSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE)

        // Инициализация UI элементов
        tempUnitGroup = findViewById(R.id.temp_unit_group)
        pushNotificationsSwitch = findViewById(R.id.push_notifications_switch)
        themeSwitch = findViewById(R.id.theme_switch)

        // Пример использования sharedPreferences для установки состояния переключателей
        val isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", false)
        pushNotificationsSwitch.isChecked = isNotificationsEnabled

        // Пример для радио-кнопок
        val tempUnit = sharedPreferences.getString("temperature_unit", "Celsius")
        if (tempUnit == "Fahrenheit") {
            tempUnitGroup.check(R.id.fahrenheit_option)
        } else {
            tempUnitGroup.check(R.id.celsius_option)
        }

        // Пример для темы
        val isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false)
        themeSwitch.isChecked = isDarkModeEnabled

        // Логика обработки переключения настроек
        pushNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications_enabled", isChecked).apply()
        }

        tempUnitGroup.setOnCheckedChangeListener { _, checkedId ->
            val temperatureUnit = if (checkedId == R.id.celsius_option) "Celsius" else "Fahrenheit"
            sharedPreferences.edit().putString("temperature_unit", temperatureUnit).apply()
        }

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
            // Применение темы
            setAppTheme(isChecked)
        }
    }

    // Метод для смены темы
    private fun setAppTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            setTheme(R.style.Theme_WeatherForecast_Dark)
        } else {
            setTheme(R.style.Theme_WeatherForecast)
        }
    }
}
