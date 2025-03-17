package com.mrgoodcat.weathertestapp.data.model.weather_model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class WeatherLocalModel(
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null,
    var name:String? = null,
    var type:String? = null
) : Parcelable