package com.mrgoodcat.weathertestapp.presentation.detail_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrgoodcat.weathertestapp.domain.model.BaseScreenDataState
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.use_case.SendGlobalStringErrorUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.details_screen.GetWeatherFromDbByCityUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.details_screen.RemoveWeatherFromDbByCityUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.details_screen.SaveWeatherInDbUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.home_screen.GetSingleWeatherByCityFromApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val sendGlobalStringErrorUseCase: SendGlobalStringErrorUseCase,
    private val getSingleWeatherByCityFromApiUseCase: GetSingleWeatherByCityFromApiUseCase,
    private val saveWeatherInDbUseCase: SaveWeatherInDbUseCase,
    private val getWeatherFromDbByCityIdUseCase: GetWeatherFromDbByCityUseCase,
    private val removeWeatherFromDbByCityUseCase: RemoveWeatherFromDbByCityUseCase,
) : ViewModel() {

    val disposableBag = CompositeDisposable()
    private val _screenState: MutableLiveData<BaseScreenDataState> = MutableLiveData()
    val screenState: LiveData<BaseScreenDataState> = _screenState

    private fun sendGlobalError(error: String?) {
        sendGlobalStringErrorUseCase.execute(error)
    }

    fun updateScreenById(city: String = "1") {
        disposableBag.add(
            getWeatherFromDbByCityIdUseCase
                .execute(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Timber.e("doOnSubscribe getWeatherFromDbByCityIdUseCase")
                    _screenState.value = BaseScreenDataState.Loading
                }
                .subscribe({ data ->
                    Timber.e("getWeatherFromDbByCityIdUseCase $data")

                    if (data.isPresent) {
                        _screenState.value = BaseScreenDataState.Success(data.get())
                    } else {
                        _screenState.value = BaseScreenDataState.Empty
                    }
                },
                    { error ->
                        Timber.e("getWeatherFromDbByCityIdUseCase error $error")
                        sendGlobalError(error.message)
                        _screenState.value = BaseScreenDataState.Error(error)
                    }
                )
        )
    }

    fun saveCurrentWeather(): Completable {
        return Completable.create { complete ->
            val state = _screenState.value
            state?.let {
                if (it !is BaseScreenDataState.Success<*>) {
                    _screenState.value = BaseScreenDataState.Error(null)
                    complete.onComplete()
                    clearDisposable()
                    return@let
                }

                val data = it.data

                _screenState.value = BaseScreenDataState.Loading

                disposableBag.add(
                    saveWeatherInDbUseCase
                        .execute(data as WeatherBaseModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            _screenState.value = BaseScreenDataState.Success(data)
                            complete.onComplete()
                            clearDisposable()
                        }
                        .subscribe({}, {
                            _screenState.value = BaseScreenDataState.Error(it)
                            complete.onError(it)
                            clearDisposable()
                        })
                )
            }
        }
    }

    fun unSaveCurrentWeather(): Completable {
        return Completable.create { complete ->
            val state = _screenState.value
            state?.let {
                if (it !is BaseScreenDataState.Success<*>) {
                    complete.onComplete()
                    return@create
                }

                val model = it.data

                _screenState.value = BaseScreenDataState.Loading

                disposableBag.add(
                    removeWeatherFromDbByCityUseCase
                        .execute(model as WeatherBaseModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            _screenState.value = BaseScreenDataState.Success(model)
                            clearDisposable()
                            complete.onComplete()
                        }
                        .subscribe({}, {
                            Timber.e(it)
                            clearDisposable()
                            complete.onError(it)
                        })
                )
            }
        }
    }

    fun updateScreenByName(city: String) {
        disposableBag.add(
            getSingleWeatherByCityFromApiUseCase
                .execute(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Timber.e("doOnSubscribe getWeatherByCityUseCase")
                    _screenState.value = BaseScreenDataState.Loading
                }
                .subscribe({ data ->
                    Timber.e("getWeatherByCityUseCase $data")
                    _screenState.value = BaseScreenDataState.Success(data)
                }, { e ->
                    Timber.e("getWeatherByCityUseCase error $e")
                    sendGlobalError(e.message)
                    _screenState.value = BaseScreenDataState.Error(e)
                })
        )
    }

    private fun clearDisposable() {
        disposableBag.clear()
    }

    override fun onCleared() {
        clearDisposable()
        Timber.e("onCleared DetailViewModel")
        super.onCleared()
    }
}