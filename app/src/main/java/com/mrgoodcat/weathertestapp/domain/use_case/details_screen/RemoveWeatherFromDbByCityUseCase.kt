package com.mrgoodcat.weathertestapp.domain.use_case.details_screen

import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.model.toWeatherBaseLocalModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RemoveWeatherFromDbByCityUseCase @Inject constructor(
    private val dbRepository: DbRepository
) {
    fun execute(model: WeatherBaseModel): Completable {
        return dbRepository.deleteWeather(model.toWeatherBaseLocalModel())
    }
}