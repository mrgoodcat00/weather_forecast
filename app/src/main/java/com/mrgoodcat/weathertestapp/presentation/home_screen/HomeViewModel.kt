package com.mrgoodcat.weathertestapp.presentation.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mrgoodcat.weathertestapp.domain.model.BaseScreenDataState
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.use_case.SendGlobalStringErrorUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.GetSingleWeatherByCityFromApiUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.GetSingleWeatherByLocationFromApiUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.SaveMainWeatherInDbUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.SubscribeOnCurrentLocationUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.SubscribeOnCurrentWeatherDbMainLocationUseCase
import com.mrgoodcat.weathertestapp.presentation.home_screen.HomeViewModel.StateParams.ScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sendGlobalStringErrorUseCase: SendGlobalStringErrorUseCase,
    private val subscribeOnCurrentLocationUseCase: SubscribeOnCurrentLocationUseCase,
    private val saveMainWeatherInDbUseCase: SaveMainWeatherInDbUseCase,
    private val getSingleWeatherByCityFromApiUseCase: GetSingleWeatherByCityFromApiUseCase,
    private val getSingleWeatherByLocationFromApiUseCase: GetSingleWeatherByLocationFromApiUseCase,
    private val subscribeOnCurrentWeatherDbMainLocationUseCase: SubscribeOnCurrentWeatherDbMainLocationUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val disposableBag = CompositeDisposable()
    private val _screenState: MutableLiveData<BaseScreenDataState> = savedStateHandle.getLiveData(
        HOME_STATE_KEY, BaseScreenDataState.Loading
    )
    val screenState: LiveData<BaseScreenDataState> = _screenState
    var isLocalPermissionGiven: Boolean = false

    private fun sendGlobalError(error: String?) {
        sendGlobalStringErrorUseCase.execute(error)
    }

    private fun updateMainScreenWithDefaultLocation(city: String? = null) {
        disposableBag.add(
            getSingleWeatherByCityFromApiUseCase
                .execute(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Timber.e("doOnSubscribe getWeatherByCityUseCase")
                    _screenState.value = BaseScreenDataState.Loading
                }
                .flatMap { data ->
                    saveMainWeatherInDbUseCase
                        .execute(data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .materialize<WeatherBaseModel>()
                }
                .subscribe(
                    { data ->
                        Timber.e("updateMainScreenWithDefaultLocation $data")
                    },
                    { error ->
                        Timber.e("updateMainScreenWithDefaultLocation error $error")
                        sendGlobalError(error.message)
                        _screenState.value = BaseScreenDataState.Error(error)
                    }
                )
        )
    }

    private fun updateMainScreenWithRealtimeLocation() {
        disposableBag.add(
            subscribeOnCurrentLocationUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Timber.e("doOnSubscribe subscribeOnCurrentLocationUseCase")
                    _screenState.value = BaseScreenDataState.Loading
                }
                .doOnDispose {
                    Timber.e("doOnDispose subscribeOnCurrentLocationUseCase")
                }
                .flatMap { location ->
                    getSingleWeatherByLocationFromApiUseCase
                        .execute(location)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            Timber.e("doOnSubscribe getWeatherByLocationUseCase")
                        }
                        .doOnDispose {
                            Timber.e("doOnDispose getWeatherByLocationUseCase")
                        }
                        .map { it.apply { it.id = 1 } }
                        .flatMap { weather ->
                            saveMainWeatherInDbUseCase
                                .execute(weather)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnDispose {
                                    Timber.e("doOnDispose getWeatherByLocationUseCase")
                                }.materialize<WeatherBaseModel>()
                        }.toObservable()
                }
                .subscribe({}, {
                    Timber.e(it)
                })
        )
    }

    fun initMainSubscriber() {
        disposableBag.add(
            subscribeOnCurrentWeatherDbMainLocationUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Timber.e("doOnSubscribe subscribeCurrentDbLocationUseCase")
                }
                .subscribe({ data ->
                    Timber.e("subscribeCurrentDbLocationUseCase subscribe $data")
                    if (data.isPresent) {
                        _screenState.value = BaseScreenDataState.Success(
                            HomeScreenState(
                                ScreenData(data.get())
                            )
                        )
                    } else {
                        _screenState.value = BaseScreenDataState.Empty
                    }
                },
                    { error ->
                        //TODO map error
                        Timber.e("subscribeCurrentDbLocationUseCase error $error")
                        _screenState.value = BaseScreenDataState.Error(error)
                    }
                )
        )
    }

    fun updateLocation() {
        if (isLocalPermissionGiven)
            updateMainScreenWithRealtimeLocation()
        else
            updateMainScreenWithDefaultLocation()
    }

    fun unsubscribeAll() {
        disposableBag.clear()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared HomeViewModel")
        unsubscribeAll()
    }

    data class HomeScreenState(
        val data: ScreenData = ScreenData()
    ) : Serializable

    sealed class StateParams : Serializable {
        data class ScreenData(val value: WeatherBaseModel = WeatherBaseModel()) : StateParams()
    }

    companion object {
        private const val HOME_STATE_KEY = "home_state_key"
    }
}