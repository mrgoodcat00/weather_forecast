package com.mrgoodcat.weathertestapp.data.model.weather_model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class RainLocalModel(
    val oneH: Double? = null
) : Parcelable