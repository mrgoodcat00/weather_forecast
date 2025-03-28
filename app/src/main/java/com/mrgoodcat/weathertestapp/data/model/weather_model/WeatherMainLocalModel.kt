package com.mrgoodcat.weathertestapp.data.model.weather_model

import WeatherMainModel
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class WeatherMainLocalModel(
    val temp: Double? = null,
    val feelsLike: Double? = null,
    val tempMin: Double? = null,
    val tempMax: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val seaLevel: Int? = null,
    val grndLevel: Int? = null
) : Parcelable

fun WeatherMainLocalModel.toWeatherMainModel(): WeatherMainModel {
    return WeatherMainModel(
        temp = this.temp,
        feelsLike = this.feelsLike,
        tempMin = this.tempMin,
        tempMax = this.tempMax,
        pressure = this.pressure,
        humidity = this.humidity,
        seaLevel = this.seaLevel,
        grndLevel = this.grndLevel
    )
}