package com.mrgoodcat.weathertestapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mrgoodcat.weathertestapp.data.db.dao.AppSettingsDao
import com.mrgoodcat.weathertestapp.data.db.dao.WeatherDao
import com.mrgoodcat.weathertestapp.data.model.AppSettingsLocalModel
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel

@Database(
    entities = [
        WeatherBaseLocalModel::class,
        AppSettingsLocalModel::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(WeatherTypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
    abstract fun getSettingsDao(): AppSettingsDao
}

