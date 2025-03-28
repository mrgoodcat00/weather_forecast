package com.mrgoodcat.weathertestapp.presentation

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mrgoodcat.weathertestapp.R
import com.mrgoodcat.weathertestapp.databinding.ActivityMainBinding
import com.mrgoodcat.weathertestapp.presentation.error.ErrorBottomSheet
import com.mrgoodcat.weathertestapp.presentation.home_screen.HomeViewModel
import com.mrgoodcat.weathertestapp.presentation.base_screen.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var binding: ActivityMainBinding

    private val baseViewModel: BaseViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private var errorBottomSheet: ErrorBottomSheet? = null
    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        errorBottomSheet = ErrorBottomSheet(this)

        subscribeOnErrors()

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { list ->
            list.forEach {
                if (!it.value) {
                    baseViewModel.sendGlobalError(this.getString(R.string.permission_declined_error))
                    homeViewModel.isLocalPermissionGiven = false
                    return@registerForActivityResult
                }
                homeViewModel.isLocalPermissionGiven = true

                reSubscribeToAll()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableBag.clear()
    }

    private fun reSubscribeToAll() {
        homeViewModel.unsubscribeAll()
        homeViewModel.initMainSubscriber()
        homeViewModel.updateLocation()
    }

    private fun subscribeOnErrors() {
        disposableBag.add(
            baseViewModel
                .subscribeOnErrors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    errorBottomSheet?.dismiss()
                }
                .subscribe(
                    { data ->
                        errorBottomSheet?.setError(data)
                        errorBottomSheet?.show()
                    }, {
                        Timber.e(it)
                    }
                )
        )
    }

    private fun checkPermissions() {
        val fineLocation = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        val coarseLocation = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
        when {
            (fineLocation == PackageManager.PERMISSION_GRANTED
                    && coarseLocation == PackageManager.PERMISSION_GRANTED) -> {
                Timber.e("PERMISSION_GRANTED checked true")
                homeViewModel.isLocalPermissionGiven = true
                reSubscribeToAll()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                ACCESS_FINE_LOCATION
            ) -> {
                Timber.e("shouldShowRequestPermissionRationale")
                baseViewModel.sendGlobalError(getString(R.string.permission_declined_error))
                homeViewModel.isLocalPermissionGiven = false
                reSubscribeToAll()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                ACCESS_COARSE_LOCATION
            ) -> {
                Timber.e("shouldShowRequestPermissionRationale")
                baseViewModel.sendGlobalError(getString(R.string.permission_declined_error))
                homeViewModel.isLocalPermissionGiven = false
                reSubscribeToAll()
            }

            else -> {
                Timber.e("requestPermissions")
                requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            )
        )
    }
}