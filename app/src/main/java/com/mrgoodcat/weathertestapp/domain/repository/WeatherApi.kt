package com.mrgoodcat.weathertestapp.domain.repository

import android.location.Location
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import io.reactivex.rxjava3.core.Single

interface WeatherApi {
    fun getWeatherByLocation(coord: Location): Single<WeatherBaseLocalModel>
    fun getWeatherByCity(city: String): Single<WeatherBaseLocalModel>
}