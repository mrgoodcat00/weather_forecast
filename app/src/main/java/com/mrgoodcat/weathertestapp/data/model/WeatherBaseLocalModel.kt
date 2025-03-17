package com.mrgoodcat.weathertestapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mrgoodcat.weathertestapp.data.db.WeatherTypeConverter
import com.mrgoodcat.weathertestapp.data.model.weather_model.CloudsLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.CoordinatesLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.RainLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherMainLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.WeatherSystemLocalModel
import com.mrgoodcat.weathertestapp.data.model.weather_model.WindLocalModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "weather")
data class WeatherBaseLocalModel(
    @PrimaryKey
    @ColumnInfo(name = "weather_id") var id: Int = 0,
    var name: String? = null,
    var cod: Int? = null,
    var visibility: Int? = null,
    var base: String? = null,

    @Embedded
    var coord: CoordinatesLocalModel? = CoordinatesLocalModel(),

    @TypeConverters(WeatherTypeConverter::class)
    var weather: List<WeatherLocalModel> = ArrayList(),

    @Embedded
    var main: WeatherMainLocalModel? = WeatherMainLocalModel(),

    @Embedded
    var wind: WindLocalModel? = WindLocalModel(),

    @Embedded
    var rain: RainLocalModel? = RainLocalModel(),

    @Embedded
    var clouds: CloudsLocalModel? = CloudsLocalModel(),

    @Embedded
    var sys: WeatherSystemLocalModel? = WeatherSystemLocalModel()
) : Parcelable