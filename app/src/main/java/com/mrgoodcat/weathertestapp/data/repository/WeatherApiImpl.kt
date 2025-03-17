package com.mrgoodcat.weathertestapp.data.repository

import android.content.Context
import android.location.Location
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import com.mrgoodcat.weathertestapp.data.network.NetworkService
import com.mrgoodcat.weathertestapp.domain.repository.WeatherApi
import io.reactivex.rxjava3.core.Single

class WeatherApiImpl(
    val context: Context,
    val networkService: NetworkService
) : WeatherApi {

    override fun getWeatherByLocation(coord: Location): Single<WeatherBaseLocalModel> {
        return networkService.getWeatherByLocation(
            coord.latitude.toString(),
            coord.longitude.toString()
        )
    }

    override fun getWeatherByCity(city: String): Single<WeatherBaseLocalModel> {
        return networkService.getWeatherByCity(city)
    }
}