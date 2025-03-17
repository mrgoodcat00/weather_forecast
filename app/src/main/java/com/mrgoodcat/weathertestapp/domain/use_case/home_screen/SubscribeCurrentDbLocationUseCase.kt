package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Observable
import java.util.Optional
import javax.inject.Inject

class SubscribeCurrentDbLocationUseCase @Inject constructor(
    private val dbRepository: DbRepository
) {
    fun execute(): Observable<Optional<WeatherBaseLocalModel>> {
        return dbRepository.getWeatherById("1")
    }
}