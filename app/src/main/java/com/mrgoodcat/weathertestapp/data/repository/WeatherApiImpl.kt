package com.mrgoodcat.weathertestapp.data.repository

import android.content.Context
import android.location.Location
import com.mrgoodcat.weathertestapp.data.model.toWeatherBaseModel
import com.mrgoodcat.weathertestapp.data.network.NetworkService
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.repository.WeatherApi
import io.reactivex.rxjava3.core.Single

class WeatherApiImpl(
    val context: Context,
    val networkService: NetworkService
) : WeatherApi {

    override fun getWeatherByLocation(coord: Location): Single<WeatherBaseModel> {
        return networkService.getWeatherByLocation(
            coord.latitude.toString(),
            coord.longitude.toString()
        ).map { it.toWeatherBaseModel() }
    }

    override fun getWeatherByCity(city: String): Single<WeatherBaseModel> {
        return networkService.getWeatherByCity(city).map { it.toWeatherBaseModel() }
    }
}