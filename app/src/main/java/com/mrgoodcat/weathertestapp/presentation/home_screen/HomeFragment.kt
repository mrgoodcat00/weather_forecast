package com.mrgoodcat.weathertestapp.presentation.home_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil3.load
import com.mrgoodcat.weathertestapp.R
import com.mrgoodcat.weathertestapp.data.model.WeatherBaseLocalModel
import com.mrgoodcat.weathertestapp.data.network.Constants.Companion.API_WEATHER_IMAGES_URL
import com.mrgoodcat.weathertestapp.databinding.FragmentMainLayoutBinding
import com.mrgoodcat.weathertestapp.domain.model.ScreenDataState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.math.RoundingMode
import java.net.UnknownHostException
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentMainLayoutBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val disposableBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposableBag.add(
            viewModel
                .subscribeOnScreenData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { data ->
                        when (data) {
                            is ScreenDataState.Error -> {
                                Timber.e("Error $data")
                                binding.currentLocationProgress.visibility = View.GONE
                                binding.currentLocationNoData.visibility = View.GONE

                                if (data.error is NoSuchElementException) {
                                    binding.currentLocationNoData.visibility = View.VISIBLE
                                }

                                if (data.error is UnknownHostException) {
                                    binding.currentLocationNoData.visibility = View.VISIBLE
                                }
                            }

                            ScreenDataState.Loading -> {
                                Timber.e("Loading")
                                binding.currentLocationProgress.visibility = View.VISIBLE
                                binding.currentLocationNoData.visibility = View.GONE
                            }

                            is ScreenDataState.Success -> {
                                Timber.e("Success $data")
                                binding.currentLocationProgress.visibility = View.GONE
                                binding.currentLocationNoData.visibility = View.GONE
                                fillData(view.context, data.data)
                            }

                            ScreenDataState.Empty -> {
                                Timber.e("Empty")
                                binding.currentLocationProgress.visibility = View.GONE
                                binding.currentLocationNoData.visibility = View.VISIBLE
                            }
                        }
                    },
                    { error ->
                        Timber.e("error $error")
                        binding.currentLocationProgress.visibility = View.GONE
                        binding.currentLocationNoData.visibility = View.VISIBLE
                    }
                )
        )

        val handler: ObservableOnSubscribe<String> = object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                binding.citySearchView.setOnQueryTextListener(
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean {
                            emitter.onNext(p0 ?: "")
                            return true
                        }

                        override fun onQueryTextChange(p0: String?): Boolean {
                            emitter.onNext(p0 ?: "")
                            return true
                        }
                    })
            }

        }

        val res = Observable
            .create(handler)
            .filter { text -> !text.isEmpty() && text.length >= 3}
            .debounce(1500, TimeUnit.MILLISECONDS)
            .map { text -> text.lowercase().trim() }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Timber.d(it)
                viewModel.updateMainScreenWithDefaultLocation(it)
            }

        binding.citySearchView.setOnSearchClickListener({
            Timber.e("setOnSearchClickListener")
        })

        binding.citySearchView.setOnCloseListener({
            viewModel.updateMainScreenWithDefaultLocation()
            true
        })

        binding.citySearchView.setOnClickListener({
            Timber.e("setOnClickListener")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposableBag.clear()
    }

    private fun fillData(ctx: Context, data: WeatherBaseLocalModel) {
        binding.currentLocationDegrees.text = data.main?.temp?.let {
            val df = DecimalFormat("#.#°")
            df.roundingMode = RoundingMode.CEILING
            df.format(it)
        } ?: "0.0°"
        binding.currentLocationTitle.text = data.name ?: ctx.getString(R.string.unknown_location)
        binding.currentLocationWeatherIcon.load("$API_WEATHER_IMAGES_URL${data.weather[0].icon}.png")
        binding.currentLocationWeatherDescription.text = data.weather[0].description
    }
}