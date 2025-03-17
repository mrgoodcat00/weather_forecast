package com.mrgoodcat.weathertestapp.domain.repository

import android.location.Location
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LocationApi {
    fun getCurrentLocation(): Single<Location>
    fun subscribeOnLocationChanges() : Observable<Location>
}