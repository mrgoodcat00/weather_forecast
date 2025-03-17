package com.mrgoodcat.weathertestapp.presentation.home_screen

import androidx.lifecycle.ViewModel
import com.mrgoodcat.weathertestapp.domain.model.ScreenDataState
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.GetCurrentLocationUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.GetWeatherByCityUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.SaveWeatherUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.SendGlobalStringErrorUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.SubscribeCurrentDbLocationUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.SubscribeOnCurrentLocationUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.SubscribeToErrorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val subscribeToErrorUseCase: SubscribeToErrorUseCase,
    private val sendGlobalStringErrorUseCase: SendGlobalStringErrorUseCase,
    private val subscribeOnCurrentLocationUseCase: SubscribeOnCurrentLocationUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val saveWeatherUseCase: SaveWeatherUseCase,
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val subscribeCurrentDbLocationUseCase: SubscribeCurrentDbLocationUseCase,
) : ViewModel() {

    private val disposableBag = CompositeDisposable()
    private val currentLocationData = PublishSubject.create<ScreenDataState>()

    init {
        currentLocationData.onNext(ScreenDataState.Loading)

        disposableBag.add(
            subscribeCurrentDbLocationUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { data ->
                        Timber.d("subscribeCurrentDbLocationUseCase onNext ${data}")
                        if (data.isPresent) {
                            currentLocationData.onNext(
                                ScreenDataState.Success(data.get())
                            )
                        } else {
                            currentLocationData.onNext(
                                ScreenDataState.Empty
                            )
                        }
                    },
                    { error ->
                        //TODO map error
                        Timber.d("subscribeCurrentDbLocationUseCase error $error")
                        currentLocationData.onNext(
                            ScreenDataState.Error(error)
                        )
                    }
                )
        )
    }

    fun subscribeOnErrors(): Observable<String> {
        return subscribeToErrorUseCase.execute()
    }

    fun sendGlobalError(error: String) {
        sendGlobalStringErrorUseCase.execute(error)
    }

    fun updateMainScreenWithDefaultLocation() {
        disposableBag.add(
            getWeatherByCityUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { data ->
                        Timber.d("updateMainScreenWithDefaultLocation $data")
                        disposableBag.add(
                            saveWeatherUseCase
                                .execute(data)
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                        )
                    },
                    { error ->
                        Timber.d("updateMainScreenWithDefaultLocation error $error")
                    }
                )
        )
    }

    fun updateMainScreenWithRealtimeLocation() {
        updateMainScreenWithDefaultLocation()
    }

    fun subscribeOnScreenData(): Observable<ScreenDataState> {
        return currentLocationData
    }

    override fun onCleared() {
        disposableBag.clear()
        super.onCleared()
    }
}