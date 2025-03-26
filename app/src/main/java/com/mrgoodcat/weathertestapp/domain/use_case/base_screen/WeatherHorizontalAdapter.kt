package com.mrgoodcat.weathertestapp.domain.use_case.base_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mrgoodcat.weathertestapp.databinding.WeatherListItemBinding
import com.mrgoodcat.weathertestapp.domain.model.WeatherBaseModel
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat

class WeatherHorizontalAdapter : RecyclerView.Adapter<WeatherHorizontalAdapter.ViewHolder>() {
    private var data: MutableList<WeatherBaseModel> = mutableListOf()
    val repoClickIntent: PublishSubject<WeatherBaseModel> = PublishSubject.create()

    fun setData(newData: List<WeatherBaseModel>) {
        val diffResult = DiffUtil.calculateDiff(WeatherDiffCallback(data, newData))
        data.clear()
        data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WeatherListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        when (val latest = payloads.lastOrNull()) {
            is UpdatePayload.UpdateTemp -> {
                holder.bindTemp(latest.data.toString())
            }

            else -> {
                onBindViewHolder(holder, position)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position], repoClickIntent)

    class ViewHolder(val binding: WeatherListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            weather: WeatherBaseModel,
            repoClickIntent: PublishSubject<WeatherBaseModel>
        ) = with(itemView) {
            val temp = (weather.main?.temp ?: "0.0°").toString()

            bindTemp(temp)

            binding.cityName.text = weather.name

            setOnClickListener { repoClickIntent.onNext(weather) }
        }

        fun bindTemp(temp: String) {
            val df = DecimalFormat("#.#°")
            df.roundingMode = RoundingMode.CEILING
            binding.cityTemp.text = df.format(temp.toDouble())
        }
    }
}