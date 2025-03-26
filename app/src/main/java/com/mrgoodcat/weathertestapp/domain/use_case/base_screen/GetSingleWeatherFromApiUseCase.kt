package com.mrgoodcat.weathertestapp.domain.use_case.base_screen

import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.repository.WeatherApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetSingleWeatherFromApiUseCase @Inject constructor(
    private val api: WeatherApi
) {
    fun execute(city:String): Single<WeatherBaseModel> {
        return api.getWeatherByCity(city)
    }
}