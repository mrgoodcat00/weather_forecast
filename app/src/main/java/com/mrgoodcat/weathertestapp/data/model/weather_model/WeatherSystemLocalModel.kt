package com.mrgoodcat.weathertestapp.data.model.weather_model

import WeatherSystemModel
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class WeatherSystemLocalModel(
    val type: Int? = null,
    val id: Int? = null,
    val country: String? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null
) : Parcelable

fun WeatherSystemLocalModel.toWeatherSystemModel(): WeatherSystemModel {
    return WeatherSystemModel(
        type = this.type,
        id = this.id,
        country = this.country,
        sunrise = this.sunrise,
        sunset = this.sunset
    )
}