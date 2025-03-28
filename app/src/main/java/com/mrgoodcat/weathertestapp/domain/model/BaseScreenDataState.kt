package com.mrgoodcat.weathertestapp.domain.model

import java.io.Serializable


sealed class BaseScreenDataState : Serializable {
    data class Success<T>(val data: T) : BaseScreenDataState()
    object Loading : BaseScreenDataState()
    object Empty : BaseScreenDataState()
    data class Error(val error: Any?) : BaseScreenDataState()
    data class OnOnTimeEvent<T>(val data: T) : BaseScreenDataState()
}