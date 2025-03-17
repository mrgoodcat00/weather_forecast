package com.mrgoodcat.weathertestapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "app_settings")
data class AppSettingsLocalModel(
    @PrimaryKey
    @ColumnInfo(name = "object_id") val id: Int = 1,
) : Parcelable