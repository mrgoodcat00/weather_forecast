package com.mrgoodcat.weathertestapp.data.repository

import com.mrgoodcat.weathertestapp.data.db.WeatherDatabase
import com.mrgoodcat.weathertestapp.data.model.AppSettingsLocalModel
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.Optional

class DbRepositoryImpl(
    val db: WeatherDatabase
) : DbRepository {
    override fun insertWeather(model: WeatherBaseLocalModel): Completable {
        return db.getWeatherDao().insertWeather(model)
    }

    override fun getWeatherById(id: String): Observable<Optional<WeatherBaseLocalModel>> {
        return db.getWeatherDao().getWeatherById(id)
    }

    override fun updateWeather(model: WeatherBaseLocalModel): Completable {
        return db.getWeatherDao().updateWeather(model)
    }

    override fun getAllWeathers(): Single<List<WeatherBaseLocalModel>> {
        return db.getWeatherDao().getAllWeathers()
    }

    override fun getAppSettings(): Single<AppSettingsLocalModel> {
        return db.getSettingsDao().getAppSettings()
    }

    override fun updateAppSettings(model: AppSettingsLocalModel): Completable {
        return db.getSettingsDao().insertAppSettings(model)
    }
}