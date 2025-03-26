package com.mrgoodcat.weathertestapp.domain.use_case.details_screen

import com.mrgoodcat.weathertestapp.data.model.toWeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Single
import java.util.Optional
import javax.inject.Inject

class GetWeatherFromDbByCityUseCase @Inject constructor(
    private val dbRepository: DbRepository
) {
    fun execute(cityId: String? = null): Single<Optional<WeatherBaseModel>> {
        val city = cityId ?: ""
        return dbRepository.getSingleWeatherById(city).map { data ->
            if (data.isPresent) {
                Optional.of(data.get().toWeatherBaseModel())
            } else {
                Optional.empty()
            }
        }
    }
}