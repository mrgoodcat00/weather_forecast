package com.mrgoodcat.weathertestapp.presentation.detail_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil3.load
import com.mrgoodcat.weathertestapp.R
import com.mrgoodcat.weathertestapp.data.network.Constants.Companion.API_WEATHER_IMAGES_URL
import com.mrgoodcat.weathertestapp.databinding.FragmentDetailLayoutBinding
import com.mrgoodcat.weathertestapp.domain.model.BaseScreenDataState
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.HttpException
import timber.log.Timber
import java.math.RoundingMode
import java.net.UnknownHostException
import java.text.DecimalFormat

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailLayoutBinding
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    private val disposableBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailLayoutBinding.inflate(inflater)
        binding.additionalLocationBackButton.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel.screenState.observe(viewLifecycleOwner, { data ->
            when (data) {
                is BaseScreenDataState.Error -> {
                    Timber.e("Error $data")
                    binding.additionalLocationProgress.visibility = View.GONE
                    binding.additionalLocationNoData.visibility = View.GONE

                    if (data.error is NoSuchElementException) {
                        binding.additionalLocationNoData.visibility = View.VISIBLE
                    }

                    if (data.error is UnknownHostException) {
                        binding.additionalLocationNoData.visibility = View.VISIBLE
                    }

                    if (data.error is HttpException) {
                        binding.additionalLocationNoData.visibility = View.VISIBLE
                        backToMain()
                    }
                }

                BaseScreenDataState.Loading -> {
                    Timber.e("Loading")
                    binding.additionalLocationProgress.visibility = View.VISIBLE
                    binding.additionalLocationNoData.visibility = View.GONE
                }

                is BaseScreenDataState.Success<*> -> {
                    binding.additionalLocationProgress.visibility = View.GONE
                    binding.additionalLocationNoData.visibility = View.GONE
                    fillData(view.context, data.data as WeatherBaseModel)
                }

                BaseScreenDataState.Empty -> {
                    Timber.e("Empty")
                    binding.additionalLocationProgress.visibility = View.GONE
                    binding.additionalLocationNoData.visibility = View.VISIBLE
                }

                is BaseScreenDataState.OnOnTimeEvent<*> -> {
                    Timber.e("OnOnTimeEvent ${data.data}")
                }
            }
        })

        if (args.cityName.isEmpty()) {
            binding.additionalLocationSave.setImageResource(R.drawable.baseline_remove_circle_outline_24)
            binding.additionalLocationSave.setOnClickListener {
                val subscribe = detailViewModel
                    .unSaveCurrentWeather()
                    .doOnComplete {
                        backToMain()
                    }
                    .subscribe({}, {
                        Timber.e(it)
                    })
                disposableBag.add(subscribe)
            }
            detailViewModel.updateScreenById(args.cityId)
        } else {
            binding.additionalLocationSave.setImageResource(R.drawable.outline_add_24)
            binding.additionalLocationSave.setOnClickListener {
                val subscribe = detailViewModel
                    .saveCurrentWeather()
                    .doOnComplete {
                        backToMain()
                    }
                    .subscribe({}, {
                        Timber.e(it)
                    })
                disposableBag.add(subscribe)
            }
            detailViewModel.updateScreenByName(args.cityName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposableBag.clear()
    }

    fun fillData(ctx: Context, data: WeatherBaseModel) {
        binding.additionalLocationDegrees.text = data.main?.temp?.let {
            val df = DecimalFormat("#.#°")
            df.roundingMode = RoundingMode.CEILING
            df.format(it)
        } ?: "0.0°"
        binding.additionalLocationTitle.text = getString(
            R.string.location_title_prefix_additional_screen,
            data.name ?: (ctx.getString(R.string.unknown_location)),
            data.sys?.country ?: ""
        )
        binding.additionalLocationWeatherIcon.load("$API_WEATHER_IMAGES_URL${data.weather[0].icon}.png")
        binding.additionalLocationWeatherDescription.text = data.weather[0].description
        binding.additionalLocationVisibility.text =
            getString(R.string.visibilty_text, data.visibility)
        binding.additionalLocationPressure.text =
            getString(R.string.pressure_text, data.main?.pressure)
    }

    private fun backToMain() {
        Timber.e("disposableBag.size ${detailViewModel.disposableBag.size()}")
        val action = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
        findNavController().navigate(action)
    }

}