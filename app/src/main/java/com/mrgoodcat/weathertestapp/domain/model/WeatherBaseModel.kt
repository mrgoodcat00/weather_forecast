package com.mrgoodcat.weathertestapp.domain.model

import WeatherMainModel
import WeatherModel
import WeatherSystemModel
import WindModel
import android.os.Parcelable
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import kotlinx.parcelize.Parcelize
import toWeatherLocalModel
import toWeatherMainLocalModel
import toWeatherSystemLocalModel
import toWindLocalModel

@Parcelize
data class WeatherBaseModel(
    var id: Int = 0,
    var name: String? = null,
    var visibility: Int? = null,
    var base: String? = null,

    var weather: List<WeatherModel> = ArrayList(),

    var main: WeatherMainModel? = WeatherMainModel(),

    var wind: WindModel? = WindModel(),

    var sys: WeatherSystemModel? = WeatherSystemModel()
) : Parcelable

fun WeatherBaseModel.toWeatherBaseLocalModel(): WeatherBaseLocalModel {
    return WeatherBaseLocalModel(
        id = this.id,
        name = this.name,
        visibility = this.visibility,
        base = this.base,
        weather = this.weather.map { it.toWeatherLocalModel() },
        main = this.main?.toWeatherMainLocalModel(),
        wind = this.wind?.toWindLocalModel(),
        sys = this.sys?.toWeatherSystemLocalModel()
    )
}