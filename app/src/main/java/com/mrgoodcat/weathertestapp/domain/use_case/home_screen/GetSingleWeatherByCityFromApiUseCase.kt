package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import android.content.Context
import com.mrgoodcat.weathertestapp.R
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.repository.WeatherApi
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetSingleWeatherByCityFromApiUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherApi: WeatherApi
) {
    fun execute(cityName: String? = null): Single<WeatherBaseModel> {
        val city = cityName ?: context.getString(R.string.default_city)
        return weatherApi.getWeatherByCity(city)
    }
}