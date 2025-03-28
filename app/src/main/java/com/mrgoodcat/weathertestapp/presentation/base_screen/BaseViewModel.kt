package com.mrgoodcat.weathertestapp.presentation.base_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mrgoodcat.weathertestapp.domain.model.BaseScreenDataState
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.domain.use_case.SendGlobalStringErrorUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.SubscribeToErrorUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.base_screen.GetAllWeatherFromDbUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.base_screen.GetSingleWeatherFromApiUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.base_screen.SubscribeOnDbLocationUseCase
import com.mrgoodcat.weathertestapp.domain.use_case.details_screen.SaveWeatherInDbUseCase
import com.mrgoodcat.weathertestapp.presentation.base_screen.BaseViewModel.StateParams.AdapterItems
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.io.Serializable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val subscribeOnDbLocationUseCase: SubscribeOnDbLocationUseCase,
    private val subscribeToErrorUseCase: SubscribeToErrorUseCase,
    private val sendGlobalStringErrorUseCase: SendGlobalStringErrorUseCase,
    private val getSingleWeatherFromApiUseCase: GetSingleWeatherFromApiUseCase,
    private val saveWeatherInDbUseCase: SaveWeatherInDbUseCase,
    private val getAllWeatherFromDbUseCase: GetAllWeatherFromDbUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _screenState: MutableLiveData<BaseScreenDataState> = savedStateHandle.getLiveData(
        BASE_STATE_KEY,
        BaseScreenDataState.Loading
    )
    private val disposableBag = CompositeDisposable()

    val clickedWeather: PublishSubject<WeatherBaseModel> = PublishSubject.create()
    val screenState: LiveData<BaseScreenDataState> = _screenState

    private fun subscribeOnDbWeather(): Flowable<List<WeatherBaseModel>> {
        return subscribeOnDbLocationUseCase.execute()
    }

    private fun getAllItemsDbWeather(): Single<List<WeatherBaseModel>> {
        return getAllWeatherFromDbUseCase.execute()
    }

    private fun saveWeatherInDb(model: WeatherBaseModel): Completable {
        return saveWeatherInDbUseCase.execute(model)
    }

    private fun getWeatherByCity(city: String): Single<WeatherBaseModel> {
        return getSingleWeatherFromApiUseCase.execute(city)
    }

    fun subscribeOnErrors(): Observable<String> {
        return subscribeToErrorUseCase.execute()
    }

    fun sendGlobalError(error: String?) {
        sendGlobalStringErrorUseCase.execute(error)
    }

    fun clickedWeatherFromAdapter(weather: WeatherBaseModel) {
        clickedWeather.onNext(weather)
    }

    fun subscribeOnWeatherFromDbBase() {
        disposableBag.add(
            subscribeOnDbWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { data ->
                        Timber.e("subscribeOnDbWeather data:$data")
                        _screenState.value = BaseScreenDataState
                            .Success(
                                BaseScreenState(
                                    AdapterItems(data)
                                )
                            )
                    },
                    { error ->
                        Timber.e("$error")
                    }
                )
        )

        disposableBag.add(
            Observable
                .interval(2, TimeUnit.MINUTES)
                .map {
                    Timber.e("tick")
                    Observable
                        .create { observable ->
                            val subscribe = getAllItemsDbWeather()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map { resource ->
                                    val iterator = resource.listIterator()
                                    while (iterator.hasNext()) {
                                        val item = iterator.next()
                                        if (item.id != 1) {
                                            item.name?.let {
                                                getWeatherByCity(it)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe({ data ->
                                                        observable.onNext(data)
                                                    }, {
                                                        Timber.e(it)
                                                    })
                                            }
                                        }
                                    }
                                }.subscribe({}, {
                                    observable.onError(it)
                                    Timber.e(it)
                                })
                            disposableBag.add(subscribe)
                        }
                        .flatMapCompletable { data ->
                            saveWeatherInDb(data)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                        }.subscribe()
                }.subscribe()
        )
    }

    fun unSubscribeOnWeatherFromDbBase() {
        disposableBag.clear()
    }

    data class BaseScreenState(
        val adapterItems: AdapterItems = AdapterItems()
    ) : Serializable

    sealed class StateParams : Serializable {
        data class AdapterItems(val value: List<WeatherBaseModel> = emptyList()) : StateParams()
    }

    companion object {
        private const val BASE_STATE_KEY = "base_state_key"
    }
}