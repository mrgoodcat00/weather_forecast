package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import android.content.Context
import android.location.Location
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import com.mrgoodcat.weathertestapp.domain.repository.WeatherApi
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetWeatherByLocationUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherApi: WeatherApi
) {
    fun execute(location: Location): Single<WeatherBaseLocalModel> {
        return weatherApi.getWeatherByLocation(location)
    }
}