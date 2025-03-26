package com.mrgoodcat.weathertestapp.domain.use_case.base_screen

import com.mrgoodcat.weathertestapp.data.model.toWeatherBaseModelList
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAllWeatherFromDbUseCase @Inject constructor(
    private val dbRepository: DbRepository
) {
    fun execute(): Single<List<WeatherBaseModel>> {
        return dbRepository.getAllWeathers().map { it.toWeatherBaseModelList() }
    }
}