package com.mrgoodcat.weathertestapp.domain.use_case.base_screen

import com.mrgoodcat.weathertestapp.data.model.toWeatherBaseModelList
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class SubscribeOnDbLocationUseCase @Inject constructor(
    private val dbRepository: DbRepository
) {
    fun execute(): Flowable<List<WeatherBaseModel>> {
        return dbRepository.subscribeAllWeathers("1").map {
            it.toWeatherBaseModelList()
        }
    }
}