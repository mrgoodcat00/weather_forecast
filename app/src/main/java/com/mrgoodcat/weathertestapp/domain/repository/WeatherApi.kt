package com.mrgoodcat.weathertestapp.domain.repository

import android.location.Location
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import io.reactivex.rxjava3.core.Single

interface WeatherApi {
    fun getWeatherByLocation(coord: Location): Single<WeatherBaseModel>
    fun getWeatherByCity(city: String): Single<WeatherBaseModel>
}