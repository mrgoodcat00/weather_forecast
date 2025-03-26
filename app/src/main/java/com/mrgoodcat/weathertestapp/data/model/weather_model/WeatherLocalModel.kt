package com.mrgoodcat.weathertestapp.data.model.weather_model

import WeatherModel
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class WeatherLocalModel(
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null,
    var name: String? = null,
) : Parcelable

fun WeatherLocalModel.toWeatherModel(): WeatherModel {
    return WeatherModel(
        main = this.main,
        description = this.description,
        icon = this.icon,
        name = this.name
    )
}

fun List<WeatherLocalModel>.toListWeatherModel(): List<WeatherModel> {
    return this.map { it.toWeatherModel() }
}