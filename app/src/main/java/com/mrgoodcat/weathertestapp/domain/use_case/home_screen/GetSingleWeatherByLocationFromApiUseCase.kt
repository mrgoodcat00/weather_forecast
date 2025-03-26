package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import android.location.Location
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.repository.WeatherApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetSingleWeatherByLocationFromApiUseCase @Inject constructor(
    private val weatherApi: WeatherApi
) {
    fun execute(location: Location): Single<WeatherBaseModel> {
        return weatherApi.getWeatherByLocation(location)
    }
}