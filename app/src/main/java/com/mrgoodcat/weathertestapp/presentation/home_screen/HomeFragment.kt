package com.mrgoodcat.weathertestapp.presentation.home_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import coil3.load
import com.mrgoodcat.weathertestapp.R
import com.mrgoodcat.weathertestapp.data.network.Constants.Companion.API_WEATHER_IMAGES_URL
import com.mrgoodcat.weathertestapp.databinding.FragmentMainLayoutBinding
import com.mrgoodcat.weathertestapp.domain.model.BaseScreenDataState
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import com.mrgoodcat.weathertestapp.presentation.base_screen.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.math.RoundingMode
import java.net.UnknownHostException
import java.text.DecimalFormat

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentMainLayoutBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val baseViewModel: BaseViewModel by activityViewModels()
    private val disposableBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Timber.e("onStart HomeFragment")
        homeViewModel.initMainSubscriber()
        homeViewModel.updateLocation()
    }

    override fun onStop() {
        homeViewModel.unsubscribeAll()
        disposableBag.clear()
        Timber.e("onStop HomeFragment")
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.screenState.observe(viewLifecycleOwner, { data ->
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
                }

                BaseScreenDataState.Loading -> {
                    Timber.e("Loading")
                    binding.additionalLocationProgress.visibility = View.VISIBLE
                    binding.additionalLocationNoData.visibility = View.GONE
                }

                is BaseScreenDataState.Success<*> -> {
                    binding.additionalLocationProgress.visibility = View.GONE
                    binding.additionalLocationNoData.visibility = View.GONE
                    fillData(data.data as WeatherBaseModel)
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

        val handler: ObservableOnSubscribe<String> = object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                binding.citySearchView.setOnQueryTextListener(
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean {
                            emitter.onNext(p0 ?: "")
                            return true
                        }

                        override fun onQueryTextChange(p0: String?): Boolean {
                            return true
                        }
                    })
            }

        }

        val res = Observable
            .create(handler)
            .filter { text -> !text.isEmpty() && text.length >= 3 }
            .map { text -> text.lowercase().trim() }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Timber.e(it)

                if (it.isEmpty()) {
                    return@subscribe
                }

                binding.citySearchView.setQuery("", false)
                binding.citySearchView.isIconified = true

                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(cityName = it)
                findNavController(view).navigate(action)
            }

        disposableBag.add(
            baseViewModel
                .clickedWeather
                .subscribe({
                    val weatherId = it.id.toString()
                    val action = HomeFragmentDirections
                        .actionHomeFragmentToDetailFragment(cityId = weatherId)
                    findNavController(view).navigate(action)
                }, {
                    Timber.e(it)
                })
        )

    }

    private fun fillData(data: WeatherBaseModel) {
        binding.additionalLocationDegrees.text = data.main?.temp?.let {
            val df = DecimalFormat("#.#°")
            df.roundingMode = RoundingMode.CEILING
            df.format(it)
        } ?: "0.0°"
        binding.additionalLocationTitle.text = getString(
            R.string.location_title_prefix,
            data.name ?: (getString(R.string.unknown_location)),
            data.sys?.country ?: ""
        )
        binding.additionalLocationWeatherIcon.load("$API_WEATHER_IMAGES_URL${data.weather[0].icon}.png")
        binding.additionalLocationWeatherDescription.text = data.weather[0].description
        binding.additionalLocationVisibility.text =
            getString(R.string.visibilty_text, data.visibility)
        binding.additionalLocationPressure.text =
            getString(R.string.pressure_text, data.main?.pressure)
    }
}