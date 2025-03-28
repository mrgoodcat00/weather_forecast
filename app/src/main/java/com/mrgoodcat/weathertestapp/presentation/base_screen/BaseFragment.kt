package com.mrgoodcat.weathertestapp.presentation.base_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrgoodcat.weathertestapp.databinding.FragmentBaseLayoutBinding
import com.mrgoodcat.weathertestapp.domain.model.BaseScreenDataState
import com.mrgoodcat.weathertestapp.domain.use_case.base_screen.WeatherHorizontalAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class BaseFragment : Fragment() {
    private lateinit var binding: FragmentBaseLayoutBinding
    private val baseViewModel: BaseViewModel by activityViewModels()
    private val weatherAdapter = WeatherHorizontalAdapter()
    private val disposableBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseLayoutBinding.inflate(inflater)
        return binding.baseLayoutRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposableBag.add(
            weatherAdapter.repoClickIntent.subscribe {
                baseViewModel.clickedWeatherFromAdapter(it)
            }
        )

        binding.savedCitiesList.adapter = weatherAdapter

        binding.savedCitiesList.setLayoutManager(
            LinearLayoutManager(
                view.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )

        baseViewModel.screenState.observe(viewLifecycleOwner, { data ->
            when (data) {
                is BaseScreenDataState.Error -> {
                    Timber.e("Error $data")
                }

                BaseScreenDataState.Loading -> {
                    Timber.e("Loading")
                }

                is BaseScreenDataState.Success<*> -> {
                    Timber.e("BaseScreenDataState data:$data")
                    val state = data.data as BaseViewModel.BaseScreenState
                    weatherAdapter.setData(state.adapterItems.value)
                }

                BaseScreenDataState.Empty -> {
                    Timber.e("Empty")
                }

                is BaseScreenDataState.OnOnTimeEvent<*> -> {
                    Timber.e("OnOnTimeEvent ${data.data}")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposableBag.clear()
    }

    override fun onPause() {
        super.onPause()
        baseViewModel.unSubscribeOnWeatherFromDbBase()
    }

    override fun onResume() {
        super.onResume()
        baseViewModel.subscribeOnWeatherFromDbBase()
    }

}