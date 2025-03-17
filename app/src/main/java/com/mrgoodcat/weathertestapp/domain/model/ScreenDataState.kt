package com.mrgoodcat.weathertestapp.domain.model

import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel

sealed class ScreenDataState {
    data class Success(val data: WeatherBaseLocalModel) : ScreenDataState()
    data object Loading : ScreenDataState()
    data object Empty : ScreenDataState()
    data class Error(val error: Any?) : ScreenDataState()
}