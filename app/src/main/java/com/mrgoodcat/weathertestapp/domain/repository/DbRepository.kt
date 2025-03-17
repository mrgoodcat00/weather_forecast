package com.mrgoodcat.weathertestapp.domain.repository

import com.mrgoodcat.weathertestapp.data.model.AppSettingsLocalModel
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.Optional

interface DbRepository {

    /* -------- Weather --------- */
    fun insertWeather(model: WeatherBaseLocalModel): Completable

    fun getWeatherById(id: String): Observable<Optional<WeatherBaseLocalModel>>

    fun updateWeather(model: WeatherBaseLocalModel): Completable

    fun getAllWeathers(): Single<List<WeatherBaseLocalModel>>

    /* -------- AppSettings ------- */

    fun getAppSettings(): Single<AppSettingsLocalModel>

    fun updateAppSettings(model: AppSettingsLocalModel): Completable
}