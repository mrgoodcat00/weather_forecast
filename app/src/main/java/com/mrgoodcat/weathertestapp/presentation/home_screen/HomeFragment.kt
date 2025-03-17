package com.mrgoodcat.weathertestapp.presentation.home_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

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
                                Timber.d("Error $data")
                                binding.currentLocationProgress.visibility = View.GONE
                                binding.currentLocationNoData.visibility = View.GONE

                                if (data.error is NoSuchElementException) {
                                    binding.currentLocationNoData.visibility = View.VISIBLE
                                }
                            }

                            ScreenDataState.Loading -> {
                                Timber.d("Loading")
                                binding.currentLocationProgress.visibility = View.VISIBLE
                                binding.currentLocationNoData.visibility = View.GONE
                            }

                            is ScreenDataState.Success -> {
                                Timber.d("Success")
                                binding.currentLocationProgress.visibility = View.GONE
                                binding.currentLocationNoData.visibility = View.GONE
                                fillData(view.context, data.data)
                            }

                            ScreenDataState.Empty -> {
                                Timber.d("Empty")
                                binding.currentLocationProgress.visibility = View.GONE
                                binding.currentLocationNoData.visibility = View.VISIBLE
                            }
                        }
                    }, { error ->
                        Timber.d("error $error")
                        binding.currentLocationProgress.visibility = View.GONE
                        binding.currentLocationNoData.visibility = View.VISIBLE
                    }
                )
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposableBag.clear()
    }

    private fun fillData(ctx: Context, data: WeatherBaseLocalModel) {
        binding.currentLocationTitle.text = data.name ?: ctx.getString(R.string.unknown_location)
        binding.currentLocationDegrees.text = data.main?.temp.toString()
        binding.currentLocationWeatherIcon.load("$API_WEATHER_IMAGES_URL${data.weather[0].icon}.png")
        binding.currentLocationWeatherDescription.text = data.weather[0].description
    }

}