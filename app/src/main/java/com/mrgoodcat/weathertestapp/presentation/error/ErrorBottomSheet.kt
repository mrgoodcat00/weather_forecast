package com.mrgoodcat.weathertestapp.presentation.error

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mrgoodcat.weathertestapp.R
import com.mrgoodcat.weathertestapp.databinding.DialogBottomSheetErrorBinding

class ErrorBottomSheet(context: Context) : BottomSheetDialog(context) {

    private val binding: DialogBottomSheetErrorBinding = DialogBottomSheetErrorBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.errorButton.setOnClickListener {
            dismiss()
        }
        setContentView(binding.root)
        this.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    fun setError(text: String) {
        binding.errorText.text = text
    }
}