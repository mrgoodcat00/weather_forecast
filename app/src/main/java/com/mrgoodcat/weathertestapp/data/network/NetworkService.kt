package com.mrgoodcat.weathertestapp.data.network

import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("weather/")
    fun getWeatherByCity(
        @Query("q") query: String,
    ): Single<WeatherBaseLocalModel>

    @GET("weather/")
    fun getWeatherByLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): Single<WeatherBaseLocalModel>
}