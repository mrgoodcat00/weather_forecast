package com.mrgoodcat.weathertestapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.Optional

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: WeatherBaseLocalModel) : Completable

    @Query("SELECT * FROM weather")
    fun getAllWeathers(): Single<List<WeatherBaseLocalModel>>

    @Query("SELECT * FROM weather WHERE weather_id == :id LIMIT 1")
    fun getWeatherById(id: String): Observable<Optional<WeatherBaseLocalModel>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWeather(weather: WeatherBaseLocalModel) : Completable

    @Query("DELETE FROM weather")
    fun clearDb() : Completable
}