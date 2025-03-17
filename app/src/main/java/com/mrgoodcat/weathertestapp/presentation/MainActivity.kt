package com.mrgoodcat.weathertestapp.presentation

import android.Manifest
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
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val viewModel: HomeViewModel by viewModels()

    private var errorBottomSheet: ErrorBottomSheet? = null

    lateinit var binding: ActivityMainBinding

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.main)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        errorBottomSheet = ErrorBottomSheet(this)

        subscribeOnErrors()

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
                if (!permission) {
                    viewModel.sendGlobalError(this.getString(R.string.permission_declined_error))
                    viewModel.updateMainScreenWithDefaultLocation()
                } else {
                    viewModel.updateMainScreenWithRealtimeLocation()
                }
            }

        checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableBag.clear()
    }

    private fun subscribeOnErrors() {
        disposableBag.add(
            viewModel
                .subscribeOnErrors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { data ->
                        errorBottomSheet?.setError(data)
                        errorBottomSheet?.show()
                    }
                )
        )
    }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
               viewModel.updateMainScreenWithRealtimeLocation()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                Timber.d("shouldShowRequestPermissionRationale")
                viewModel.sendGlobalError(getString(R.string.permission_declined_error))
                viewModel.updateMainScreenWithDefaultLocation()
            }

            else -> {
                Timber.d("requestPermissions")
                requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}