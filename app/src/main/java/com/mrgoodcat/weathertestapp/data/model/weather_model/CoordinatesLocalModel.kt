package com.mrgoodcat.weathertestapp.data.model.weather_model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class CoordinatesLocalModel(
    val lon: Double? = null,
    val lat: Double? = null
) : Parcelable