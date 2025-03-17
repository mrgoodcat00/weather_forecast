package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import android.location.Location
import com.mrgoodcat.weathertestapp.domain.repository.LocationApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationApi: LocationApi
) {
    fun execute(): Single<Location> {
        return locationApi.getCurrentLocation()
    }
}