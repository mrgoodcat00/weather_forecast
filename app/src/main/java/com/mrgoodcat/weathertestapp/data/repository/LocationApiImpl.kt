package com.mrgoodcat.weathertestapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.mrgoodcat.weathertestapp.domain.repository.LocationApi
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import timber.log.Timber

@SuppressLint("MissingPermission")
class LocationApiImpl(context: Context) : LocationApi {

    private val client = LocationServices.getFusedLocationProviderClient(context)
    //TODO change to proper timeout
    private val locationRequest = LocationRequest
        .Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
        .build()

    private lateinit var locationCallback: LocationCallback

    override fun getCurrentLocation(): Single<Location> {
        return Single.create { result ->
                client.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                            CancellationTokenSource().token

                        override fun isCancellationRequested() = false
                    },
                ).addOnSuccessListener {
                    Timber.e("getCurrentLocation OnSuccess $it")
                    result.onSuccess(it)
                }.addOnFailureListener {
                    Timber.e("getCurrentLocation OnSuccess $it")
                    result.onError(it)
                }
            }
    }

    override fun subscribeOnLocationChanges(): Observable<Location> {
        return Observable.create { observable ->
            if (::locationCallback.isInitialized) {
                client.removeLocationUpdates(locationCallback)
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    //Timber.e("onLocationResult $locationResult")

                    locationResult.lastLocation?.let {
                        observable.onNext(it)
                        Timber.e(locationResult.lastLocation.toString())
                    }
                }
            }

            client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }.doOnDispose {
            Timber.e("subscribeOnLocationChanges doOnDispose")

            if (::locationCallback.isInitialized)
                client.removeLocationUpdates(locationCallback)
        }
    }
}