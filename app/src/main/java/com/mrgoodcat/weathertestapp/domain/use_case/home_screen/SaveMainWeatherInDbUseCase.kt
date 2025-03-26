package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.model.toWeatherBaseLocalModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveMainWeatherInDbUseCase @Inject constructor(
    private val dbRepository: DbRepository,
) {
    fun execute(weather: WeatherBaseModel): Completable {
        weather.id = 1
        return dbRepository.insertWeather(weather.toWeatherBaseLocalModel())
    }
}