package com.mrgoodcat.weathertestapp.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherLocalModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

@ProvidedTypeConverter
class WeatherTypeConverter (val moshi: Moshi) {
    @TypeConverter
    fun fromListToString(list: List<WeatherLocalModel>): String {
        val type: ParameterizedType = Types.newParameterizedType(
            List::class.java,
            WeatherLocalModel::class.java
        )
        val adapter: JsonAdapter<List<WeatherLocalModel>> = moshi.adapter(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun fromStringToLst(value: String): List<WeatherLocalModel> {
        val type: ParameterizedType = Types.newParameterizedType(
            List::class.java,
            WeatherLocalModel::class.java,
        )
        val adapter: JsonAdapter<List<WeatherLocalModel>> = moshi.adapter(type)
        return adapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromListToInt(list: List<Int>): String {
        val type: ParameterizedType = Types.newParameterizedType(
            List::class.java,
            Int::class.java
        )
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun fromIntToLst(value: String): List<Int> {
        val type: ParameterizedType = Types.newParameterizedType(
            List::class.java,
            WeatherLocalModel::class.java,
        )
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(type)
        return adapter.fromJson(value)!!
    }
}