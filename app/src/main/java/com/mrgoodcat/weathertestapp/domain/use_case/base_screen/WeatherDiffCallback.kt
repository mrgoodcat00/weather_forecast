package com.mrgoodcat.weathertestapp.domain.use_case.base_screen

import androidx.recyclerview.widget.DiffUtil
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel

class WeatherDiffCallback(
    private val data: MutableList<WeatherBaseModel>,
    private val newData: List<WeatherBaseModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return data.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return data[oldItemPosition].id == newData[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return data[oldItemPosition].main?.temp == newData[newItemPosition].main?.temp
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return UpdatePayload.UpdateTemp(newData[newItemPosition].main?.temp)
    }
}

sealed class UpdatePayload {
    data class UpdateTemp(val data: Double?) : UpdatePayload()
}