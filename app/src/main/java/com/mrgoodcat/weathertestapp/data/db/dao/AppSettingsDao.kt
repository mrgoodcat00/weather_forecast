package com.mrgoodcat.weathertestapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrgoodcat.weathertestapp.data.model.AppSettingsLocalModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface AppSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppSettings(settings: AppSettingsLocalModel) : Completable

    @Query("SELECT * FROM app_settings")
    fun getAppSettings(): Single<AppSettingsLocalModel>

    @Query("DELETE FROM app_settings")
    fun clearDb() : Completable
}