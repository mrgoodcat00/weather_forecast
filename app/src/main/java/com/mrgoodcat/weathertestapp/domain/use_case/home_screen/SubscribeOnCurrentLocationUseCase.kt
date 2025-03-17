package com.mrgoodcat.weathertestapp.domain.use_case.home_screen

import android.location.Location
import com.mrgoodcat.weathertestapp.domain.repository.LocationApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SubscribeOnCurrentLocationUseCase @Inject constructor(
    private val locationApi: LocationApi
) {
    fun execute(): Observable<Location> {
        return locationApi.subscribeOnLocationChanges()
    }
}