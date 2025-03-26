package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import com.mrgoodcat.weathertestapp.data.model.toWeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Observable
import java.util.Optional
import javax.inject.Inject

class SubscribeOnCurrentWeatherDbMainLocationUseCase @Inject constructor(
    private val dbRepository: DbRepository
) {
    fun execute(): Observable<Optional<WeatherBaseModel>> {
        return dbRepository
            .getWeatherById("1")
            .map { data ->
                if (data.isPresent) {
                    Optional.of(data.get().toWeatherBaseModel())
                } else {
                    Optional.empty()
                }
            }
    }
}