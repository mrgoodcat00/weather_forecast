package com.mrgoodcat.weathertestapp.data.model.weather_model

import WindModel
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class WindLocalModel(
    val speed: Double? = null,
    val deg: Int? = null,
    val gust: Double? = null
) : Parcelable

fun WindLocalModel.toWindLocalModel(): WindModel {
    return WindModel(
        speed = this.speed,
        deg = this.deg,
        gust = this.gust
    )
}