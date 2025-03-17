package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveWeatherUseCase @Inject constructor(
    private val dbRepository: DbRepository,
) {
    fun execute(weather: WeatherBaseLocalModel): Completable {
        weather.id = 1
        return dbRepository.insertWeather(weather)

    }
}