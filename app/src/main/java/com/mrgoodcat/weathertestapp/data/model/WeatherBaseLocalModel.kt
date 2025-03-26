package com.mrgoodcat.weathertestapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mrgoodcat.weathertestapp.data.db.WeatherTypeConverter
import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherMainLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherSystemLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.WindLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.toWeatherMainModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.toWeatherModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.toWeatherSystemModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.toWindLocalModel
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "weather")
data class WeatherBaseLocalModel(
    @PrimaryKey
    @ColumnInfo(name = "weather_id") var id: Int = 0,
    var name: String? = null,
    var visibility: Int? = null,
    var base: String? = null,

    @TypeConverters(WeatherTypeConverter::class)
    var weather: List<WeatherLocalModel> = ArrayList(),

    @Embedded
    var main: WeatherMainLocalModel? = WeatherMainLocalModel(),

    @Embedded
    var wind: WindLocalModel? = WindLocalModel(),

    @Embedded
    var sys: WeatherSystemLocalModel? = WeatherSystemLocalModel()
) : Parcelable

fun WeatherBaseLocalModel.toWeatherBaseModel(): WeatherBaseModel {
    return WeatherBaseModel(
        id = this.id,
        name = this.name,
        visibility = this.visibility,
        base = this.base,
        weather = this.weather.map { it.toWeatherModel() },
        main = this.main?.toWeatherMainModel(),
        wind = this.wind?.toWindLocalModel(),
        sys = this.sys?.toWeatherSystemModel()
    )
}

fun List<WeatherBaseLocalModel>.toWeatherBaseModelList(): List<WeatherBaseModel> {
    return this.map { it.toWeatherBaseModel() }
}

